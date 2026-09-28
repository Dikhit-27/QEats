
/*
 *
 * * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.models.RestaurantEntity;
import com.crio.qeats.repositories.RestaurantRepository;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;

  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;

  @Autowired
  private RestaurantRepository restaurantRepository;


  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {

    if (restaurantRepositoryService == null) {
      log.error("Null");
      throw new IllegalArgumentException("resturaunt service in not initialised");
    }

    double servingRadiusInKms =
        isPeakHour(currentTime) ? peakHoursServingRadiusInKms : normalHoursServingRadiusInKms;

    // List<Restaurant> resultList =
    // restaurantRepositoryService.findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(),
    // getRestaurantsRequest.getLongitude(), currentTime, servingRadiusInKms);


    // return new GetRestaurantsResponse(resultList);


    List<Restaurant> resultList =
        restaurantRepositoryService.findAllRestaurantsCloseBy(getRestaurantsRequest.getLatitude(),
            getRestaurantsRequest.getLongitude(), currentTime, servingRadiusInKms);

    return new GetRestaurantsResponse(resultList);


  }

  private boolean isTimeWithinRange(LocalTime currentTime, LocalTime startTime, LocalTime endTime) {
    return !currentTime.isBefore(startTime) && currentTime.isBefore(endTime);
  }


  private boolean isPeakHour(LocalTime currentTime) {
    return isTimeWithinRange(currentTime, LocalTime.of(7, 59, 59), LocalTime.of(10, 00, 01))
        || isTimeWithinRange(currentTime, LocalTime.of(12, 59, 59), LocalTime.of(14, 00, 01))
        || isTimeWithinRange(currentTime, LocalTime.of(18, 59, 59), LocalTime.of(21, 00, 01));
  }
}
