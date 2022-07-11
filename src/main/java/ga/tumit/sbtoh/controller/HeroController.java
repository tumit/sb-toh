package ga.tumit.sbtoh.controller;

import ga.tumit.sbtoh.dto.HeroResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeroController {

    @GetMapping("/heroes")
    public List<HeroResponse> list() {
        return List.of(new HeroResponse(1L, "Cherprang"));
    }
}
