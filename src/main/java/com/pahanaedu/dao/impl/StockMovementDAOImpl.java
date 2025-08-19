package com.pahanaedu.dao.impl;

import com.pahanaedu.dao.interfaces.StockMovementDAO;
import com.pahanaedu.model.StockMovement;
import com.pahanaedu.constant.MovementType;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.util.DBConnection;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Implementation of StockMovementDAO interface
 * Handles all database operations for StockMovement entity
 */
public class StockMovementDAOImpl implements StockMovementDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
    
    @Override
    public int addStockMovement(StockMovement movement) throws DatabaseException {
        String sql = "INSERT INTO stock_movements (item_id, movement_type, quantity, " +
                    "reference_type, reference_id, reason, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, movement.getItemId());
            pstmt.setString(2, movement.getMovementType().name());
            pstmt.setInt(3, movement.getQuantity());
            pstmt.setString(4, movement.getReferenceType());
            pstmt.setInt(5, movement.getReferenceId());
            pstmt.setString(6, movement.getReason());
            pstmt.setInt(7, movement.getUserId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Creating stock movement failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new DatabaseException("Creating stock movement failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error adding stock movement: " + e.getMessage(), e);
        }
    }
    
    @Override
    public StockMovement getMovementById(int movementId) throws DatabaseException {
        String sql = "SELECT sm.*, i.item_code, i.item_name, i.unit_price, u.full_name as user_name " +
                    "FROM stock_movements sm " +
                    "JOIN items i ON sm.item_id = i.item_id " +
                    "LEFT JOIN users u ON sm.user_id = u.user_id " +
                    "WHERE sm.movement_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, movementId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractStockMovementFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting stock movement by ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<StockMovement> getMovementsByItemId(int itemId) throws DatabaseException {
        String sql = "SELECT sm.*, i.item_code, i.item_name, i.unit_price, u.full_name as user_name " +
                    "FROM stock_movements sm " +
                    "JOIN items i ON sm.item_id = i.item_id " +
                    "LEFT JOIN users u ON sm.user_id = u.user_id " +
                    "WHERE sm.item_id = ? " +
                    "ORDER BY sm.movement_date DESC";
        
        List<StockMovement> movements = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movements.add(extractStockMovementFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting movements by item ID: " + e.getMessage(), e);
        }
        
        return movements;
    }
    
    @Override
    public List<StockMovement> getMovementsByType(MovementType movementType) throws DatabaseException {
        String sql = "SELECT sm.*, i.item_code, i.item_name, i.unit_price, u.full_name as user_name " +
                    "FROM stock_movements sm " +
                    "JOIN items i ON sm.item_id = i.item_id " +
                    "LEFT JOIN users u ON sm.user_id = u.user_id " +
                    "WHERE sm.movement_type = ? " +
                    "ORDER BY sm.movement_date DESC";
        
        List<StockMovement> movements = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, movementType.name());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movements.add(extractStockMovementFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting movements by type: " + e.getMessage(), e);
        }
        
        return movements;
    }
    
    @Override
    public List<StockMovement> getMovementsByReference(String referenceType, int referenceId) 
            throws DatabaseException {
        String sql = "SELECT sm.*, i.item_code, i.item_name, i.unit_price, u.full_name as user_name " +
                    "FROM stock_movements sm " +
                    "JOIN items i ON sm.item_id = i.item_id " +
                    "LEFT JOIN users u ON sm.user_id = u.user_id " +
                    "WHERE sm.reference_type = ? AND sm.reference_id = ? " +
                    "ORDER BY sm.movement_date";
        
        List<StockMovement> movements = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, referenceType);
            pstmt.setInt(2, referenceId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movements.add(extractStockMovementFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting movements by reference: " + e.getMessage(), e);
        }
        
        return movements;
    }
    
    @Override
    public List<StockMovement> getMovementsByDateRange(Date startDate, Date endDate) 
            throws DatabaseException {
        String sql = "SELECT sm.*, i.item_code, i.item_name, i.unit_price, u.full_name as user_name " +
                    "FROM stock_movements sm " +
                    "JOIN items i ON sm.item_id = i.item_id " +
                    "LEFT JOIN users u ON sm.user_id = u.user_id " +
                    "WHERE DATE(sm.movement_date) BETWEEN ? AND ? " +
                    "ORDER BY sm.movement_date DESC";
        
        List<StockMovement> movements = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movements.add(extractStockMovementFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting movements by date range: " + e.getMessage(), e);
        }
        
        return movements;
    }
    
    @Override
    public List<StockMovement> getMovementsByUserId(int userId) throws DatabaseException {
        String sql = "SELECT sm.*, i.item_code, i.item_name, i.unit_price, u.full_name as user_name " +
                    "FROM stock_movements sm " +
                    "JOIN items i ON sm.item_id = i.item_id " +
                    "LEFT JOIN users u ON sm.user_id = u.user_id " +
                    "WHERE sm.user_id = ? " +
                    "ORDER BY sm.movement_date DESC";
        
        List<StockMovement> movements = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movements.add(extractStockMovementFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting movements by user ID: " + e.getMessage(), e);
        }
        
        return movements;
    }
    
    @Override
    public List<StockMovement> getRecentMovements(int limit) throws DatabaseException {
        String sql = "SELECT sm.*, i.item_code, i.item_name, i.unit_price, u.full_name as user_name " +
                    "FROM stock_movements sm " +
                    "JOIN items i ON sm.item_id = i.item_id " +
                    "LEFT JOIN users u ON sm.user_id = u.user_id " +
                    "ORDER BY sm.movement_date DESC " +
                    "LIMIT ?";
        
        List<StockMovement> movements = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movements.add(extractStockMovementFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting recent movements: " + e.getMessage(), e);
        }
        
        return movements;
    }
    
    @Override
    public List<StockMovement> getMovementsWithPagination(int offset, int limit) 
            throws DatabaseException {
        String sql = "SELECT sm.*, i.item_code, i.item_name, i.unit_price, u.full_name as user_name " +
                    "FROM stock_movements sm " +
                    "JOIN items i ON sm.item_id = i.item_id " +
                    "LEFT JOIN users u ON sm.user_id = u.user_id " +
                    "ORDER BY sm.movement_date DESC " +
                    "LIMIT ? OFFSET ?";
        
        List<StockMovement> movements = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movements.add(extractStockMovementFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting movements with pagination: " + e.getMessage(), e);
        }
        
        return movements;
    }
    
    @Override
    public int getCurrentStock(int itemId) throws DatabaseException {
        String sql = "SELECT SUM(CASE " +
                    "WHEN movement_type IN ('IN', 'RETURN', 'INITIAL') THEN quantity " +
                    "WHEN movement_type IN ('OUT', 'DAMAGE') THEN -quantity " +
                    "WHEN movement_type = 'ADJUSTMENT' THEN quantity " +
                    "ELSE 0 END) as current_stock " +
                    "FROM stock_movements " +
                    "WHERE item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("current_stock");
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting current stock: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public double getStockValue(int itemId) throws DatabaseException {
        String sql = "SELECT i.unit_price, " +
                    "SUM(CASE " +
                    "WHEN sm.movement_type IN ('IN', 'RETURN', 'INITIAL') THEN sm.quantity " +
                    "WHEN sm.movement_type IN ('OUT', 'DAMAGE') THEN -sm.quantity " +
                    "WHEN sm.movement_type = 'ADJUSTMENT' THEN sm.quantity " +
                    "ELSE 0 END) as current_stock " +
                    "FROM stock_movements sm " +
                    "JOIN items i ON sm.item_id = i.item_id " +
                    "WHERE sm.item_id = ? " +
                    "GROUP BY i.unit_price";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double unitPrice = rs.getDouble("unit_price");
                    int currentStock = rs.getInt("current_stock");
                    return unitPrice * currentStock;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting stock value: " + e.getMessage(), e);
        }
        
        return 0.0;
    }
    
    @Override
    public int getTotalMovementCount() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM stock_movements";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting total movement count: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public Map<MovementType, Integer> getMovementSummaryByType() throws DatabaseException {
        String sql = "SELECT movement_type, COUNT(*) as count " +
                    "FROM stock_movements " +
                    "GROUP BY movement_type";
        
        Map<MovementType, Integer> summary = new HashMap<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MovementType type = MovementType.valueOf(rs.getString("movement_type"));
                summary.put(type, rs.getInt("count"));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting movement summary by type: " + e.getMessage(), e);
        }
        
        return summary;
    }
    
    @Override
    public Map<Date, Integer> getMovementSummaryByDate(Date startDate, Date endDate) 
            throws DatabaseException {
        String sql = "SELECT DATE(movement_date) as date, COUNT(*) as count " +
                    "FROM stock_movements " +
                    "WHERE DATE(movement_date) BETWEEN ? AND ? " +
                    "GROUP BY DATE(movement_date) " +
                    "ORDER BY date";
        
        Map<Date, Integer> summary = new LinkedHashMap<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    summary.put(rs.getDate("date"), rs.getInt("count"));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting movement summary by date: " + e.getMessage(), e);
        }
        
        return summary;
    }
    
    @Override
    public int getStockBalanceAsOfDate(int itemId, Date upToDate) throws DatabaseException {
        String sql = "SELECT SUM(CASE " +
                    "WHEN movement_type IN ('IN', 'RETURN', 'INITIAL') THEN quantity " +
                    "WHEN movement_type IN ('OUT', 'DAMAGE') THEN -quantity " +
                    "WHEN movement_type = 'ADJUSTMENT' THEN quantity " +
                    "ELSE 0 END) as balance " +
                    "FROM stock_movements " +
                    "WHERE item_id = ? AND movement_date <= ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            pstmt.setTimestamp(2, new Timestamp(upToDate.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("balance");
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting stock balance as of date: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public List<Integer> getItemsWithMovements(Date startDate, Date endDate) 
            throws DatabaseException {
        String sql = "SELECT DISTINCT item_id " +
                    "FROM stock_movements " +
                    "WHERE DATE(movement_date) BETWEEN ? AND ? " +
                    "ORDER BY item_id";
        
        List<Integer> itemIds = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    itemIds.add(rs.getInt("item_id"));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting items with movements: " + e.getMessage(), e);
        }
        
        return itemIds;
    }
    
    @Override
    public int deleteMovementsByReference(String referenceType, int referenceId) 
            throws DatabaseException {
        String sql = "DELETE FROM stock_movements WHERE reference_type = ? AND reference_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, referenceType);
            pstmt.setInt(2, referenceId);
            
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting movements by reference: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Map<String, Object>> getStockValuationReport() throws DatabaseException {
        String sql = "SELECT i.item_id, i.item_code, i.item_name, i.unit_price, " +
                    "SUM(CASE " +
                    "WHEN sm.movement_type IN ('IN', 'RETURN', 'INITIAL') THEN sm.quantity " +
                    "WHEN sm.movement_type IN ('OUT', 'DAMAGE') THEN -sm.quantity " +
                    "WHEN sm.movement_type = 'ADJUSTMENT' THEN sm.quantity " +
                    "ELSE 0 END) as current_stock " +
                    "FROM items i " +
                    "LEFT JOIN stock_movements sm ON i.item_id = sm.item_id " +
                    "WHERE i.is_active = true " +
                    "GROUP BY i.item_id, i.item_code, i.item_name, i.unit_price " +
                    "HAVING current_stock > 0 " +
                    "ORDER BY i.item_name";
        
        List<Map<String, Object>> report = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("itemId", rs.getInt("item_id"));
                row.put("itemCode", rs.getString("item_code"));
                row.put("itemName", rs.getString("item_name"));
                row.put("unitPrice", rs.getDouble("unit_price"));
                row.put("currentStock", rs.getInt("current_stock"));
                row.put("stockValue", rs.getDouble("unit_price") * rs.getInt("current_stock"));
                report.add(row);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting stock valuation report: " + e.getMessage(), e);
        }
        
        return report;
    }
    
    @Override
    public List<StockMovement> getAuditTrail(int itemId, Date startDate, Date endDate) 
            throws DatabaseException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sm.*, i.item_code, i.item_name, i.unit_price, u.full_name as user_name ");
        sql.append("FROM stock_movements sm ");
        sql.append("JOIN items i ON sm.item_id = i.item_id ");
        sql.append("LEFT JOIN users u ON sm.user_id = u.user_id ");
        sql.append("WHERE DATE(sm.movement_date) BETWEEN ? AND ? ");
        
        if (itemId > 0) {
            sql.append("AND sm.item_id = ? ");
        }
        
        sql.append("ORDER BY sm.movement_date, sm.movement_id");
        
        List<StockMovement> movements = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            
            if (itemId > 0) {
                pstmt.setInt(3, itemId);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movements.add(extractStockMovementFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting audit trail: " + e.getMessage(), e);
        }
        
        return movements;
    }
    
    /**
     * Extract StockMovement object from ResultSet
     */
    private StockMovement extractStockMovementFromResultSet(ResultSet rs) throws SQLException {
        StockMovement movement = new StockMovement();
        movement.setMovementId(rs.getInt("movement_id"));
        movement.setItemId(rs.getInt("item_id"));
        movement.setMovementType(MovementType.valueOf(rs.getString("movement_type")));
        movement.setQuantity(rs.getInt("quantity"));
        movement.setReferenceType(rs.getString("reference_type"));
        movement.setReferenceId(rs.getInt("reference_id"));
        movement.setReason(rs.getString("reason"));
        movement.setUserId(rs.getInt("user_id"));
        movement.setMovementDate(rs.getTimestamp("movement_date"));
        
        // Set additional fields if available
        try {
            movement.setItemCode(rs.getString("item_code"));
            movement.setItemName(rs.getString("item_name"));
            movement.setUnitPrice(rs.getDouble("unit_price"));
            movement.setUserName(rs.getString("user_name"));
            
            // Calculate total value
            movement.setTotalValue(movement.calculateTotalValue());
        } catch (SQLException e) {
            // These columns might not be present in all queries
        }
        
        return movement;
    }
}