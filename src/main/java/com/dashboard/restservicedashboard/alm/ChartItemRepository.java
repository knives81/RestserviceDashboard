package com.dashboard.restservicedashboard.alm;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface ChartItemRepository extends MongoRepository<ChartItem, String> {

}
