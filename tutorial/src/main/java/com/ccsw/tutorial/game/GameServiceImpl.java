package com.ccsw.tutorial.game;

import com.ccsw.tutorial.author.AuthorService;
import com.ccsw.tutorial.category.CategoryService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    AuthorService authorService;

    @Autowired
    CategoryService categoryService;


 @Override
    public Game get(Long id) {

        return this.gameRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Game> find(String title, Long idCategory) {

        GameSpecification titleSpec = new GameSpecification(new SearchCriteria("title", ":", title));
        GameSpecification categorySpec = new GameSpecification(new SearchCriteria("category.id", ":", idCategory));

        Specification<Game> spec = Specification.where(titleSpec).and(categorySpec);

        return this.gameRepository.findAll(spec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
public void save(Long id, GameDto dto) {
    // Validar que el título del juego no sea nulo o contenga solo espacios
    if (dto.getTitle() != null && dto.getTitle().trim().isEmpty()) {
        throw new IllegalArgumentException("El título del juego no puede estar vacío ni contener solo espacios.");
    }

    // Validar que el autor no sea nulo ni negativo
    if (dto.getAuthor() == null || dto.getAuthor().getId() == null) {
        throw new IllegalArgumentException("El autor no puede ser nulo.");
    }
    if (dto.getAuthor().getId() < 0) {
        throw new IllegalArgumentException("El ID del autor no puede ser negativo.");
    }

    // Validar que la categoría no sea nula ni negativa
    if (dto.getCategory() == null || dto.getCategory().getId() == null) {
        throw new IllegalArgumentException("La categoría no puede ser nula.");
    }
    if (dto.getCategory().getId() < 0) {
        throw new IllegalArgumentException("El ID de la categoría no puede ser negativo.");
    }

    // Si el campo age es numérico y quieres validar que no sea negativo
    if (dto.getAge() != null) {
        try {
            int ageValue = Integer.parseInt(dto.getAge().trim());
            if (ageValue < 0) {
                throw new IllegalArgumentException("La edad no puede ser negativa.");
            }
        } catch (NumberFormatException e) {
        
        }
    }

    Game game;

    if (id == null) {
        game = new Game();
    } else {
        game = this.gameRepository.findById(id).orElse(null);
    }

    BeanUtils.copyProperties(dto, game, "id", "author", "category");

    game.setAuthor(authorService.get(dto.getAuthor().getId()));
    game.setCategory(categoryService.get(dto.getCategory().getId()));

    this.gameRepository.save(game);
}
}