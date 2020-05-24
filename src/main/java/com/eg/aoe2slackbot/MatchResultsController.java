package com.eg.aoe2slackbot;

import com.eg.aoe2slackbot.entity.SlackSlashCommandResponse;
import com.eg.aoe2slackbot.service.MatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aoe2/matchResults")
public class MatchResultsController {

    private static final Logger LOG = LoggerFactory.getLogger(MatchResultsController.class.getName());

    private MatchService matchService;

    @Autowired
    public MatchResultsController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping()
    public ResponseEntity<SlackSlashCommandResponse> getMostRecentMatchResults() {

        matchService.postMatchResultsToSlack();

        try {


        } catch (Exception ex) {
            LOG.error("Error getting rating.", ex);
            return new ResponseEntity<SlackSlashCommandResponse>(new SlackSlashCommandResponse("An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<SlackSlashCommandResponse>(new SlackSlashCommandResponse("Team Random Map Rank: " + "replace me!"), HttpStatus.OK);

    }

}
