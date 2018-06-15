package com.dashboard.restservicedashboard.selector;

import com.dashboard.commondashboard.Entity;
import com.dashboard.commondashboard.EntityConfAbstract;

import com.dashboard.restservicedashboard.configuration.AppProperties;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SelectorManager {
	
	private static final Logger log = LoggerFactory.getLogger(SelectorManager.class);
	
	HashMap<Entity.EntityType, MultiKeyMap> tagValuesToConfPositionIndexPerEntity = new HashMap<>();
	HashMap<Entity.EntityType, HashMap<String, Integer>> tagNameToPositionIndexPerEntity = new HashMap<>();
	HashMap<Entity.EntityType, HashMap<String, List<String>>> tagNameToTagValuesPerEntity = new HashMap<>();


	private void computeTagNameTagValues(ConfigurationService configurationService) {
		for (Entity.EntityType entityType : Entity.EntityType.values()) {
			
			EntityConfAbstract entityConf = configurationService.getEntityConf(entityType);
			

			List<String> entityTagNames = entityConf.getTagNames();
			log.info(entityTagNames.toString());
			
			List<List<String>> entityTagValues = entityConf.getTags();
			if(entityConf!=null) {
				setTagNameToPositionIndex(entityType, entityTagNames);
				setTagNameToTagValues(entityType, entityTagNames, entityTagValues);
				setTagValuesToConfPositionIndex(entityType, entityTagNames, entityTagValues);
			}

		}
	}

	public List<Selector> computeSelectors(ConfigurationService configurationService) {
		List<Selector> selectors = new ArrayList<>();
		
		computeTagNameTagValues(configurationService);
		
		for(Entity.EntityType entityType : tagNameToTagValuesPerEntity.keySet()) {

			HashMap<String, List<String>> tagNameToTagValues = tagNameToTagValuesPerEntity.get(entityType);
			List<Option> options = new ArrayList<>();
			for (Map.Entry<String, List<String>> entry : tagNameToTagValues.entrySet()) {
				options.add(Option.buildOption(entry.getKey(), entry.getValue()));
			}
			options.add(Option.buildOptionChartType());

			Selector selector = new Selector();
			selector.setEntityType(entityType);
			selector.setItems(options);
			selectors.add(selector);

		}
		return selectors;
	}

	public List<Integer> getConfPositionIndex(AppProperties appProp, Selector selector) {

		Entity.EntityType entityType = selector.getEntityType();

		HashMap<String, Integer> tagNameToPositionIndex = tagNameToPositionIndexPerEntity.get(entityType);
		HashMap<String, List<String>> tagNameToTagValues = tagNameToTagValuesPerEntity.get(entityType);

		List<List<String>> allOptionSelected = getAllOptionSelected(tagNameToTagValues, tagNameToPositionIndex,
				selector);
		List<List<String>> allCombination = computeAllCombination(allOptionSelected);

		List<MultiKey> multikeys = buildMultiKeysList(allCombination);

		return findConfPositionIndex(entityType, multikeys);
	}

	private List<MultiKey> buildMultiKeysList(List<List<String>> allCombination) {
		List<MultiKey> multikeys = new ArrayList<>();
		for (int i = 0; i < allCombination.get(0).size(); i++) {
			String[] multiKeyTags = new String[allCombination.size()];
			for (int j = 0; j < allCombination.size(); j++) {
				multiKeyTags[j] = allCombination.get(j).get(i);
			}
			multikeys.add(new MultiKey(multiKeyTags));
		}
		return multikeys;
	}

	private List<List<String>> getAllOptionSelected(HashMap<String, List<String>> tagNameToTagValues,
			HashMap<String, Integer> tagNameToPositionIndex, Selector selector) {
		List<List<String>> allOptionSelected = new ArrayList<>();

		for (Option option : selector.getItems()) {
			String tagName = option.getTagName();

			String tagSelected = option.getTagSelected();
			if (!SelectorConstant.CHART_TYPE_LABEL.equals(tagName)) {
				Integer positionIndex = tagNameToPositionIndex.get(tagName);

				if (tagSelected.equals(SelectorConstant.ALL_LABEL)) {
					List<String> allTagValues = tagNameToTagValues.get(tagName);
					allOptionSelected.add(positionIndex, allTagValues);
				} else {
					allOptionSelected.add(positionIndex, Collections.singletonList(tagSelected));
				}
			}
		}
		return allOptionSelected;
	}

	private List<Integer> findConfPositionIndex(Entity.EntityType entityType, List<MultiKey> multikeys) {
		MultiKeyMap tagValuesToConfPositionIndex = tagValuesToConfPositionIndexPerEntity.get(entityType);
		List<Integer> ids = new ArrayList<>();
		for (MultiKey multikey : multikeys) {
			Integer confPositionIndex = (int) tagValuesToConfPositionIndex.get(multikey);
			ids.add(confPositionIndex);
		}
		return ids;

	}

	private void setTagValuesToConfPositionIndex(Entity.EntityType entityType, List<String> entityTags,
                                                 List<List<String>> entityTagValues) {
		MultiKeyMap tagValuesToConfPositionIndex = new MultiKeyMap();
		int confPositionIndex = 0;
		for (List<String> tagValues : entityTagValues) {

			MultiKey multiKey = new MultiKey<>(tagValues.toArray());
			tagValuesToConfPositionIndex.put(multiKey, confPositionIndex++);
		}
		tagValuesToConfPositionIndexPerEntity.put(entityType, tagValuesToConfPositionIndex);
	}

	private void setTagNameToPositionIndex(Entity.EntityType entityType, List<String> entityConfTags) {
		HashMap<String, Integer> tagNameToPositionIndex = new HashMap<>();
		int positionIndex = 0;
		for (String tagName : entityConfTags) {
			tagNameToPositionIndex.put(tagName, positionIndex++);
		}
		tagNameToPositionIndexPerEntity.put(entityType, tagNameToPositionIndex);
	}

	private void setTagNameToTagValues(Entity.EntityType entityType, List<String> entityTagNames,
                                       List<List<String>> entityTagValues) {
		
		HashMap<String, List<String>> tagNameToTagValues = new HashMap<>();
		int tagValuePositionIndex = 0;
		for (String tagName : entityTagNames) {
			Set<String> tagValuesPerTagName = new HashSet<>();
			for (List<String> tagValues : entityTagValues) {
				tagValuesPerTagName.add(tagValues.get(tagValuePositionIndex));
			}
			tagValuePositionIndex++;
			tagNameToTagValues.put(tagName, new ArrayList<>(tagValuesPerTagName));
		}
		tagNameToTagValuesPerEntity.put(entityType, tagNameToTagValues);
	}

	private List<List<String>> computeAllCombination(List<List<String>> optionsSelected) {
		int numOfRows = 1;
		for (List<String> option : optionsSelected) {
			numOfRows *= option.size();
		}
		List<List<String>> allCombination = new ArrayList<>();

		int repetitionSameElement = numOfRows;

		for (List<String> option : optionsSelected) {

			List<String> combination = new ArrayList<>();

			int counterElement = 0;
			int repetitionArray = numOfRows / repetitionSameElement;
			repetitionSameElement = repetitionSameElement / option.size();

			int counterRepetitionArray = 0;
			int counterRepetitionSameElement = 0;

			while (counterRepetitionArray < repetitionArray) {

				while (counterElement < option.size()) {

					while (counterRepetitionSameElement < repetitionSameElement) {
						combination.add(option.get(counterElement));
						counterRepetitionSameElement++;
					}
					counterRepetitionSameElement = 0;
					counterElement++;

				}
				counterElement = 0;
				counterRepetitionArray++;

			}
			allCombination.add(combination);
		}
		return allCombination;
	}

}
