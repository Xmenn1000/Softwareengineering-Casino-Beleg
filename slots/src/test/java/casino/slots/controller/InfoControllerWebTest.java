package casino.slots.controller;

import casino.slots.service.InfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InfoController.class)
class InfoControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InfoService infoService;

    @Test
    void shouldReturnRules() throws Exception {
        String rules = """
                Slots - Game Rules
                Pull the lever and match symbols.
                """;

        when(infoService.getRules())
                .thenReturn(rules);

        mockMvc.perform(
                        get("/casino/slots/api/info/rules")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.TEXT_PLAIN
                ))
                .andExpect(content().string(rules));

        verify(infoService).getRules();
        verifyNoMoreInteractions(infoService);
    }

    @Test
    void shouldReturnChances() throws Exception {
        String chances = """
                Each spin turns 3 reels.
                CHERRY: 50 percent
                """;

        when(infoService.getChances())
                .thenReturn(chances);

        mockMvc.perform(
                        get("/casino/slots/api/info/chances")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.TEXT_PLAIN
                ))
                .andExpect(content().string(chances));

        verify(infoService).getChances();
        verifyNoMoreInteractions(infoService);
    }
}