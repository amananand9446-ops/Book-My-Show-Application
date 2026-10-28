package com.scaler.bookMyShow.Repository;

import com.scaler.bookMyShow.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}