package com.example.demo.services;

import com.example.demo.repository.DrawingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrawingObjectService {

    private final DrawingsRepository drawingsRepository;

    public DrawingObjectService(DrawingsRepository drawingsRepository) {
        this.drawingsRepository = drawingsRepository;
    }

}
