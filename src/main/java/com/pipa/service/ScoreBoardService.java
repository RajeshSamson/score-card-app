package com.pipa.service;


import com.pipa.model.UserPositionResponse;
import com.pipa.model.UserScoreRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Comparator.*;
import static java.util.Map.Entry.comparingByValue;

/**
 * This is a service class which handles the business logic.
 */
@Service
public class ScoreBoardService {

    /**
     * Here I'm using the {@code ConcurrentHashMap} to handle the multiple concurrent request.
     */
    private Map<Long, Long> scoreCard = new ConcurrentHashMap<>();

    /**
     * This method adds the user score to the global scope holder.
     *
     * @param usr - The {@code UserScoreRequest} value.
     */
    public void storeUserScore(UserScoreRequest usr) {
        if (Objects.nonNull(usr)) {
            if (scoreCard.containsKey(usr.getUserId())) {
                Long currentPoints = scoreCard.get(usr.getUserId());
                scoreCard.put(usr.getUserId(), currentPoints + usr.getPoints());
            } else {
                scoreCard.put(usr.getUserId(), usr.getPoints());
            }
        }
    }

    /**
     * Sorts the global data storage in descending order.
     *
     * @param m - The {@code Map} value.
     * @return - Returns the {@code LinkedHashMap} in the descending order based on the value.
     */
    private LinkedHashMap<Long, Long> sortedScoreCard(Map<Long, Long> m) {
        if (m.isEmpty()) {
            return new LinkedHashMap<>();
        }
        return m.entrySet().stream()
                .sorted(comparingByValue(reverseOrder()))
                .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue(),
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * Is used to calculate the position of the player.
     *
     * @param map    - The {@code LinkedHashMap} value.
     * @param userId - The current userId
     * @return Returns the position of the player.
     */
    private int getPosition(LinkedHashMap<Long, Long> map, Long userId) {
        if (map.isEmpty()) {
            return -1;
        }
        Set<Long> keys = map.keySet();
        List<Long> listKeys = new LinkedList<Long>(keys);
        return listKeys.indexOf(userId);
    }

    /**
     * This method retrieves the position of the player.
     *
     * @param usrId - The userId value.
     * @return - Returns {@code UserPositionResponse}
     */
    public UserPositionResponse getPosition(long usrId) {
        LinkedHashMap<Long, Long> sortedScoreCard = sortedScoreCard(scoreCard);
        if (Objects.isNull(sortedScoreCard) && sortedScoreCard.isEmpty()) {
            return new UserPositionResponse();
        }
        int position = getPosition(sortedScoreCard, usrId);
        if (position == -1) {
            return new UserPositionResponse();
        }
        Long score = sortedScoreCard.get(usrId);
        return new UserPositionResponse(usrId, score, Long.valueOf(position + 1));
    }

    /**
     * This method provides the {@code List} of users based on their current positions.
     *
     * @return - Returns {@code List} of {@code UserPositionResponse}.
     */
    public List<UserPositionResponse> getHighestScoreList() {
        LinkedHashMap<Long, Long> sortedScoreCard = sortedScoreCard(scoreCard);
        final List<UserPositionResponse> response = new LinkedList<>();
        if (!sortedScoreCard.isEmpty() && sortedScoreCard.size() <= 20000) {
            sortedScoreCard.entrySet().forEach(f -> {
                        long userId = f.getKey();
                        long score = f.getValue();
                        int position = getPosition(sortedScoreCard, userId);
                        response.add(new UserPositionResponse(userId, score, Long.valueOf(position + 1)));
                    }
            );
        }
        return response;
    }
}
