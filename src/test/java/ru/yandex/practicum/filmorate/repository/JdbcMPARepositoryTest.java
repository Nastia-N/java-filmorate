package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.repository.mapper.MPAMapper;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcMPARepository.class, MPAMapper.class})
public class JdbcMPARepositoryTest {
    private final JdbcMPARepository mpaRepository;

    @Test
    void isValidateGetAllGenres() {
        Collection<MpaRating> allRatings = mpaRepository.getAllRating();
        assertThat(allRatings).hasSize(5);
    }

    @Test
    void isValidateGetGenreById() {
        assertEquals("PG-13", mpaRepository.getRatingById(3).getName(), "Рейтинг не совпадает.");
    }
}
