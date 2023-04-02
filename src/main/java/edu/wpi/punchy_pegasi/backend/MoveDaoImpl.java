package edu.wpi.punchy_pegasi.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MoveDaoImpl implements IDao<Move, Long> {
    private final HashMap<Long, Move> moves;
    private final PdbController dbController = PdbController.getSingleton();

    public MoveDaoImpl() {
        moves = new HashMap<Long, Move>();
    }

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
    public void update(Move move, Object[] params) {
        var key = move.getUuid();
        Move newMove = moves.get(key);
        if (params.length != 4) {
            //TODO: throw error
        } else {
            newMove.setUuid((Long) params[0]);
            newMove.setNodeID((Long) params[1]);
            newMove.setLongName(params[2].toString());
            newMove.setDate(params[3].toString());
            moves.put(key, newMove);
        }
    }

    @Override
    public void delete(Move move) {
        var key = move.getUuid();
        moves.remove(key);
    }
}
