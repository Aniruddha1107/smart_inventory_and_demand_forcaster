package com.myproject.smartinventory.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myproject.smartinventory.entity.Forecast;
import com.myproject.smartinventory.entity.Product;

public interface ForecastRepository extends JpaRepository<Forecast, Long>{
	List<Forecast> findByProductOrderByForecastDateAsc(Product product);
}
