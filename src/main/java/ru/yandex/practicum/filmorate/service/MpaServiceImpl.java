package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.dto.MpaMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaDao mpaDao;
    private final MpaMapper mpaMapper;

    @Override
    public List<MpaDto> getAllMpa() {
        log.info("Получение всех MPAs.");
        return mpaDao.getAllMpa().stream()
                .map(mpaMapper::toDto)
                .toList();
    }

    @Override
    public MpaDto getMpaById(Integer id) {
        log.info("Получение Mpa с id={}", id);
        return mpaDao.getMpaById(id)
                .map(mpaMapper::toDto)
                .orElseThrow(() -> {
                    String msg = "Mpa с id=%d не найден".formatted(id);
                    log.warn(msg);
                    return new NotFoundException(msg);
                });
    }
}