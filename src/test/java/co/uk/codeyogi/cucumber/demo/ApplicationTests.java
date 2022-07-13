package co.uk.codeyogi.cucumber.demo;

import co.uk.codeyogi.cucumber.demo.model.Meme;
import co.uk.codeyogi.cucumber.demo.repository.MemeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class,
        ApplicationTests.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private MemeRepository memeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private List<Meme> expectedMemes;
    private List<Meme> actualMemes;

    private static final Logger logger = LogManager.getLogger(ApplicationTests.class);

    @Before
    public void setup(){
        expectedMemes=new ArrayList<>();
        actualMemes=new ArrayList<>();
        memeRepository.deleteAll();
    }

    @Test
    public void testBaseOnline() throws JsonProcessingException {
        //Obtengo la lista de documentos completa desde la base para validar la no existencia de datos
        logger.info("Obtengo la lista de documentos completa desde la base para validar la no existencia de datos");
        actualMemes.addAll(Arrays.asList(
                objectMapper.readValue(
                        testRestTemplate.getForEntity("/api/memes/all", String.class)
                                .getBody(), Meme[].class)));

        Assertions.assertTrue(actualMemes.isEmpty());
    }

    @Test
    public void crearMemesEnBase() throws JsonProcessingException {
        //Genero una lista de memes a agregar a la base
        logger.info("Genero una lista de memes a agregar a la base");
        List<Meme> memeList = new ArrayList<>();
        Meme meme1 = new Meme("MemeNuevo1", 2);
        Meme meme2 = new Meme("MemeNuevo2", 3);
        memeList.add(meme1);
        memeList.add(meme2);

        //Agrego los memes en una lista a la base
        logger.info("Agrego los memes en una lista a la base");
        expectedMemes.addAll(memeList);
        memeRepository.saveAll(memeList);

        //Obtengo los memes de la base
        logger.info("Obtengo los memes de la base");
        actualMemes.addAll(Arrays.asList(
                objectMapper.readValue(
                        testRestTemplate.getForEntity("/api/memes/all", String.class)
                                .getBody(), Meme[].class)));

        //Valido que los memes se hayan insertado correctamente en la base
        logger.info("Valido que los memes se hayan insertado correctamente en la base");
        Assertions.assertEquals(expectedMemes.size(),actualMemes.size());
    }

    @Test
    public void validaMemesEnBase() throws JsonProcessingException {
        //Limpio la lista para volver a validar
        logger.info("Limpio la lista para volver a validar");
        actualMemes.clear();

        //Obtengo los memes de la base
        logger.info("Obtengo los memes de la base");
        actualMemes.addAll(Arrays.asList(
                objectMapper.readValue(
                        testRestTemplate.getForEntity("/api/memes/all", String.class)
                                .getBody(), Meme[].class)));

        //Uso el metodo de validar memes para ver que los datos de cada uno de los registros sean los correctos
        logger.info("Uso el metodo de validar memes para ver que los datos de cada uno de los registros sean los correctos");
        validateMemes();
    }

    @Test
    public void realizoAltaNuevoMeme(){
        //Genero un nuevo objeto de meme a dar de alta
        logger.info("Genero un nuevo objeto de meme a adar de alta");
        Meme meme3 = new Meme("MemeNuevo3", 4);

        //Agrego el meme a la lista de los esperados
        logger.info("Genero un nuevo objeto de meme a adar de alta");
        expectedMemes.add(meme3);

        //Hago el post para el alta del meme en la base
        logger.info("Hago el post para el alta del meme en la base");
        testRestTemplate.postForEntity("/api/memes/new", meme3, Meme.class);

        //Valido que se haya realizado el alta
        logger.info("Valido que se haya realizado el alta");
        validateMemes();
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
