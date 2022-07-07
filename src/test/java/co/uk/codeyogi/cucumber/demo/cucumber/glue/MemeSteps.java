package co.uk.codeyogi.cucumber.demo.cucumber.glue;

import co.uk.codeyogi.cucumber.demo.model.Meme;
import co.uk.codeyogi.cucumber.demo.repository.MemeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class MemeSteps {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MemeRepository memeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Meme> expectedMemes;
    private List<Meme> actualMemes;

    @Before
    public void setup(){
        expectedMemes=new ArrayList<>();
        actualMemes=new ArrayList<>();
        memeRepository.deleteAll();
    }

    @Given("los siguientes memes")
    public void los_siguientes_memes(final List<Meme> memes) {
        expectedMemes.addAll(memes);
        memeRepository.saveAll(memes);
    }

    @When("el usuario solicita todos los memes")
    public void el_usuario_solicita_todos_los_memes() throws JsonProcessingException {
        actualMemes.addAll(Arrays.asList(
                objectMapper.readValue(
                        testRestTemplate.getForEntity("/api/memes/all", String.class)
                                .getBody(), Meme[].class)));
    }

    @Then("entonces todos los memes son devueltos")
    public void entonces_todos_los_memes_son_devueltos() {
        validateMemes();
    }

    @When("un usuario hace un post de un nuevo meme {string} con nivel {int}")
    public void un_usuario_hace_un_post_de_un_nuevo_meme_meme3_con_nivel(String meme, Integer nivel) {
        Meme expected = new Meme(meme, nivel);
        expectedMemes.add(expected);
        testRestTemplate.postForEntity("/api/memes/new", expected, Meme.class);
    }

    @Then("este se encuentra en la base de datos")
    public void este_se_encuentra_en_la_base_de_datos() {
        actualMemes.addAll(memeRepository.findAll());
        validateMemes();
    }

    @Then("posee un id")
    public void posee_un_id() {
        Assertions.assertNotNull(actualMemes.get(0).getMemeId());
    }

    private void validateMemes(){
        Assertions.assertEquals(expectedMemes.size(),actualMemes.size());
        IntStream.range(0, actualMemes.size())
                .forEach(index -> validateMeme(expectedMemes.get(index),actualMemes.get(index)));
    }

    private void validateMeme(final Meme expected, final Meme actual){
        Assertions.assertEquals(expected.getMeme(),actual.getMeme());
        Assertions.assertEquals(expected.getDankness(),actual.getDankness());
    }

}
