package ru.otus.services;

import ru.otus.model.Player;

public class PlayerServiceImpl implements PlayerService {

    private final IOService ioService;

    public PlayerServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public Player getPlayer() {
        ioService.out("Introduce yourself, please");
        String playerName = ioService.readLn("Enter the name: ");
        return new Player(playerName);
    }
}
