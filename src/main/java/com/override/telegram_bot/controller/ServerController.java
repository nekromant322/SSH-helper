package com.override.telegram_bot.controller;

import com.override.telegram_bot.dto.ServerDTO;
import com.override.telegram_bot.mapper.ServerMapper;
import com.override.telegram_bot.model.Server;
import com.override.telegram_bot.service.ServerServiceImpl;
import com.override.telegram_bot.service.SshCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/server-panel")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ServerController {

    @Autowired
    private ServerServiceImpl serverServiceImpl;

    @Autowired
    private SshCommandService sshCommandService;

    @GetMapping
    public String getServers(Model model) {
        model.addAttribute("servers", serverServiceImpl.findAllServers());
        return "admin-panel";
    }

    @GetMapping("/server/{id}")
    @ResponseBody
    public ResponseEntity<Server> getServerById(@PathVariable Long id) {
        Server server = serverServiceImpl.findServer(id);
        if (server == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(server);
    }

    @GetMapping("/allServers")
    @ResponseBody
    public List<ServerDTO> getAllServers() {
        return serverServiceImpl.findAllServers().stream().map(ServerMapper::serverToServerDTO).toList();
    }

    //TODO exceptions
    @PostMapping()
    public ResponseEntity<HttpStatus> createServer(@RequestBody @Valid ServerDTO serverDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Server server = ServerMapper.serverDTOToServer(serverDTO);
        serverServiceImpl.saveServer(server);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //TODO exceptions
    @PatchMapping("/server/{id}")
    public ResponseEntity<HttpStatus> updateServer(@RequestBody @Valid ServerDTO serverDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        serverServiceImpl.updateServer(ServerMapper.serverDTOToServer(serverDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/server/{id}")
    public ResponseEntity<HttpStatus> deleteServer(@PathVariable Long id) {
        serverServiceImpl.deleteServer(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @ResponseBody
    @GetMapping("/bash/{ip}")
    public String execCommand(@PathVariable String ip, @RequestParam String cmd) {
        return sshCommandService.execCommand(ip, cmd);
    }
}
