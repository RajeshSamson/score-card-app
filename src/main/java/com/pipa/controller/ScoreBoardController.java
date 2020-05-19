package com.pipa.controller;

import com.pipa.model.UserPositionResponse;
import com.pipa.model.UserScoreRequest;
import com.pipa.service.ScoreBoardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * This is {@code RestController} for handling the requests.
 */
@RestController
@RequestMapping("/api")
public class ScoreBoardController {

    final ScoreBoardService scoreBoardService;

    public ScoreBoardController(ScoreBoardService scoreBoardService) {
        this.scoreBoardService = scoreBoardService;
    }

    /**
     * Endpoint to add the user score.
     *
     * @param userScoreRequest - The {@code UserScoreRequest} request object.
     * @return - Returns HTTP status 201
     */
    @PostMapping("/score")
    @ApiOperation(value = "This method can be called several times per user and not return anything. The points should be\n" +
            "added to the user’s current score (score = current score + new points)")
    public ResponseEntity<?> addScore(@RequestBody UserScoreRequest userScoreRequest) {
        this.scoreBoardService.storeUserScore(userScoreRequest);
        return new ResponseEntity<>(CREATED);
    }

    /**
     * Endpoint to get the current position of the user.
     *
     * @param id - The user id value.
     * @return - Returns the {@code UserPositionResponse}
     */
    @GetMapping("/{userId}/position")
    @ApiOperation(value = "Retrieves the current position of a specific user, considering the score for all users. If a user\n" +
            "hasn't submitted a score, the response must be empty")
    public ResponseEntity<?> getPosition(@PathVariable("userId") long id) {
        UserPositionResponse position = this.scoreBoardService.getPosition(id);
        if (Objects.isNull(position.getUserId())) {
            return ResponseEntity.ok(OK);
        }
        return ResponseEntity.ok(position);
    }

    /**
     * Endpoint to return the {@code List} of {@code UserPositionResponse} based on the position
     *
     * @return - Returns {@code List} of {@code UserPositionResponse}
     */
    @GetMapping("/highscorelist")
    @ApiOperation("Retrieves the high scores list, in order, limited to the 20000 higher scores. A request for a high\n" +
            "score list without any scores submitted shall be an empty list.")
    public List<UserPositionResponse> getHighestScoreList() {
        return this.scoreBoardService.getHighestScoreList();
    }

}
