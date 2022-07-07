Feature: Memes Test Feature

  Scenario: El usuario obtiene los memes
    Given los siguientes memes
      | meme  | nivel      |
      | meme1 | 1          |
      | meme2 | 3          |
    When el usuario solicita todos los memes
    Then entonces todos los memes son devueltos

  Scenario: Un usuario hace un post de un nuevo meme
    When un usuario hace un post de un nuevo meme "meme3" con nivel 5
    Then este se encuentra en la base de datos
    And posee un id
