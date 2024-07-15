package hu.bono.bigbank.dragons.common.domain;

import hu.bono.bigbank.dragons.game.domain.Game;
import hu.bono.bigbank.dragons.investigation.domain.Reputation;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;

@Component
public class GameMapper {

    public GameSession gameToGameSession(
        final Game game,
        final CharacterSheet characterSheet
    ) {
        if (game == null) {
            return null;
        }
        if (characterSheet == null) {
            return null;
        }
        return GameSession.builder()
            .creationTimestamp(Instant.now())
            .gameId(game.gameId())
            .characterSheet(
                CharacterSheet.builder()
                    .name(characterSheet.getName())
                    .lives(game.lives())
                    .gold(game.gold())
                    .level(game.level())
                    .score(game.score())
                    .highScore(game.highScore())
                    .reputation(Reputation.builder().build())
                    .purchasedItems(new HashSet<>())
                    .myBook(
                        CharacterSheet.MyBook.builder()
                            .lives(game.lives())
                            .gold(game.gold())
                            .level(game.level())
                            .score(game.score())
                            .build()
                    )
                    .build()
            )
            .turn(game.turn())
            .shop(new HashMap<>())
            .messages(new HashSet<>())
            .expiredMessages(new HashSet<>())
            .myBook(
                GameSession.MyBook.builder()
                    .turn(game.turn())
                    .build()
            )
            .build();
    }
}
