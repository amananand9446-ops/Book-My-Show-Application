
package com.scaler.bookMyShow.service;

import com.scaler.bookMyShow.Repository.ShowRepository;
import com.scaler.bookMyShow.Repository.ShowSeatRepo;
import com.scaler.bookMyShow.model.Show;
import com.scaler.bookMyShow.model.ShowSeat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final ShowSeatRepo showSeatRepo;

    public ShowService(
            ShowRepository showRepository,
            ShowSeatRepo showSeatRepo) {
        this.showRepository = showRepository;
        this.showSeatRepo = showSeatRepo;
    }

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public List<ShowSeat> getSeatsForShow(int showId) {
        if (!showRepository.existsById(showId)) {
            throw new IllegalArgumentException(
                    "Show not found with ID: " + showId);
        }

        return showSeatRepo.findByShow_Id(showId);
    }
}
