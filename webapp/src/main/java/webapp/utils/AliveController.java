package webapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AliveController {

    private final Logger logger = LoggerFactory.getLogger(AliveController.class);

    @GetMapping("/alive")
    public ResponseEntity<?> alive() {
        logger.info("I'm alive!");
        return ResponseEntity.ok().build();
    }

}
