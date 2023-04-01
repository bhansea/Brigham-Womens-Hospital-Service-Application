package edu.wpi.punchy_pegasi.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.lang.Long;

public class MoveDaoImpl implements IDao<Move, Long> {
    private HashMap<Long, Move> moves;
    private PdbController dbController = PdbController.getSingleton();
    public MoveDaoImpl() { moves = new HashMap<Long, Move>(); }

    @Override
    public Optional<Move> get(Long key) {
        return Optional.ofNullable(moves.get(key));
    }

    @Override
    public Map<Long, Move> getAll() {
        return this.moves;
    }

    @Override
    public void save(Move move) {
        this.moves.put(move.getUuid(), move);
    }

    @Override
    public void update(Long key, Object[] params) {
        Move move = moves.get(key);
        if (params.length != 4) {
            //TODO: throw error
        } else {
            move.setUuid((Long) params[0]);
            move.setNodeID((Long) params[1]);
            move.setLongName(params[2].toString());
            move.setDate(params[3].toString());
            moves.put(key, move);
        }
    }

    @Override
    public void delete(Long key) {
        moves.remove(key);
    }
}
