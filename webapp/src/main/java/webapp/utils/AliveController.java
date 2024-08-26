package webapp.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AliveController {
    //appfw stuff
    @GetMapping("/alive")
    public ResponseEntity<?> alive() {
        return ResponseEntity.ok().build();
    }

}
