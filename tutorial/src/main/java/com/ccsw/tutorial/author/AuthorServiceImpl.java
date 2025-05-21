package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.author.model.AuthorSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Author get(Long id) {

        return this.authorRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Author> findPage(AuthorSearchDto dto) {

        return this.authorRepository.findAll(dto.getPageable().getPageable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, AuthorDto data) {
    // Validar que el nombre no sea nulo ni solo espacios
    if (data.getName() == null || data.getName().trim().isEmpty()) {
        throw new IllegalArgumentException("El nombre del autor no puede estar vacío ni contener solo espacios.");
    }
    // Validar que la nacionalidad no sea nula ni solo espacios
    if (data.getNationality() == null || data.getNationality().trim().isEmpty()) {
        throw new IllegalArgumentException("La nacionalidad del autor no puede estar vacía ni contener solo espacios.");
    }

    Author author;

    if (id == null) {
        author = new Author();
    } else {
        author = this.get(id);
    }

    BeanUtils.copyProperties(data, author, "id");

    this.authorRepository.save(author);
}

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if(this.get(id) == null){
            throw new Exception("Not exists");
        }

        this.authorRepository.deleteById(id);
    }

    /**
        * {@inheritDoc}
    */
    @Override
    public List<Author> findAll() {

    return (List<Author>) this.authorRepository.findAll();
    }

}