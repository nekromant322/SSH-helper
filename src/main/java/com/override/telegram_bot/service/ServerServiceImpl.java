package com.override.telegram_bot.service;

import com.override.telegram_bot.model.Server;
import com.override.telegram_bot.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServerServiceImpl {

    @Autowired
    private ServerRepository serverRepository;

    public List<Server> findAllServers() {
        return serverRepository.findAll();
    }

    public Server findServer(Long id) {
        Optional<Server> serverFromDb = serverRepository.findById(id);
        return serverFromDb.orElseThrow();
    }

    public Server findServerByIp(String ip) {
        Optional<Server> serverFromDb = serverRepository.findServerByIp(ip);
        return serverFromDb.orElseThrow();
    }

    public void saveServer(Server server) {
        Optional<Server> optionalServer = serverRepository.findByName(server.getName());
        if (optionalServer.isPresent()) {
            return;
        }
        serverRepository.save(server);
    }

    @Transactional
    public void updateServer(Server updatedServer) {
        Server newServer = findServer(updatedServer.getId());
        newServer.setName(updatedServer.getName());
        newServer.setIp(updatedServer.getIp());
        serverRepository.save(newServer);
    }

    @Transactional
    public void deleteServer(Long id) {
        if (serverRepository.findById(id).isPresent()) {
            serverRepository.deleteById(id);
        }
    }
}
