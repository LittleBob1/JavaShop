package com.awaken.kidsshop.modules.size.service;

import com.awaken.kidsshop.modules.size.entity.Size;
import com.awaken.kidsshop.modules.size.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SizeService {
    private final SizeRepository sizeRepository;

    @Autowired
    public SizeService(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    public List<Size> allSizes(){
        return sizeRepository.findAll();
    }

    public Size createSize(Size size){
        return sizeRepository.save(size);
    }

    public boolean deleteSize(Long sizeId) {
        if (sizeRepository.findById(sizeId).isPresent()) {
            sizeRepository.deleteById(sizeId);
            return true;
        }
        return false;
    }

    public Size updateSize(Long id, Size size){
        Size s = sizeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        s.setName(size.getName());
        return sizeRepository.save(s);
    }
}
