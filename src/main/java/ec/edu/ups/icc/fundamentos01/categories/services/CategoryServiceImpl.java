package ec.edu.ups.icc.fundamentos01.categories.services;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoryResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.dtos.CreateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categories.dtos.UpdateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findByDeletedFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategoryResponseDto findOne(Long id) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        return toResponse(entity);
    }

    @Override
    public CategoryResponseDto create(CreateCategoryDto dto) {
        // EXISTE EN LA BASE DE DATOS?
        if (categoryRepository.existsByNameIgnoreCaseAndDeletedFalse(dto.getName())) {
            throw new ConflictException("Category name already registered");
        }
        // INSTANCIAR LA ENTIDAD
        CategoryEntity entity = new CategoryEntity();
        // CARGAS LOS DATOS EN LA ENTIDAD
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        // CREAR UN OBJETO SAVED PARA GUARDAR LA ENTIDAD RECIEN CARGADA A UN METODO NATIVO LLAMADO SAVE() QUE ESTA EN REPOSITORIO
        CategoryEntity saved = categoryRepository.save(entity);

        return toResponse(saved);
    }

    @Override
    public CategoryResponseDto update(Long id, UpdateCategoryDto dto) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        CategoryEntity saved = categoryRepository.save(entity);

        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        // CREAR UNA ENTIDAD DE CATEGORIA, Y A ESTA LE PASO EL PARAMETRO ID, QUE CONSUME EL REPOSITORIO  findById()
        // PARA BUSCAR EL REGISTRO
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("Category not found");
        }

        entity.setDeleted(true);

        categoryRepository.save(entity);
    }


    // ESTO DEBERIA SER EN UN MAPPER PERO YA LO TENGO LOCAL
    // LE PASO LA ENTIDAD QUE RETORNA EL DTO RESPONSIVO
    private CategoryResponseDto toResponse(CategoryEntity entity) {
        // INSTANCIAR LA RESPUESTA
        CategoryResponseDto dto = new CategoryResponseDto();
        // AL OBJETO DTO AÑADIR EL ID, NOMBRE, Y DESCRIPCION
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        // DEVOLVER EL DTO CARGADO CON LOS OBJETOS DE RESPUESTA
        return dto;
    }
}
