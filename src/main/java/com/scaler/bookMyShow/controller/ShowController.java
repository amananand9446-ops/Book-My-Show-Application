
package com.scaler.bookMyShow.controller;

import com.scaler.bookMyShow.model.Show;
import com.scaler.bookMyShow.model.ShowSeat;
import com.scaler.bookMyShow.service.ShowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping
    public List<Show> getAllShows() {
        return showService.getAllShows();
    }

    @GetMapping("/{showId}/seats")
    public List<ShowSeat> getSeatsForShow(
            @PathVariable int showId) {
        return showService.getSeatsForShow(showId);
    }
}
