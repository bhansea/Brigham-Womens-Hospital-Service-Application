package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Employee;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class EmployeeCachedDaoImpl implements IDao<java.lang.Long, Employee, Employee.Field>, PropertyChangeListener {

    static String[] fields = {"employeeID", "firstName", "lastName"};

    private final Map<java.lang.Long, Employee> cache = new HashMap<>();
    private final PdbController dbController;

    public EmployeeCachedDaoImpl(PdbController dbController) {
        this.dbController = dbController;
        initCache();
        this.dbController.addPropertyChangeListener(this);
    }

    public EmployeeCachedDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    public void add(Employee employee) {
        if (!cache.containsKey(employee.getEmployeeID()))
            cache.put(employee.getEmployeeID(), employee);
    }

    public void update(Employee employee) {
        cache.put(employee.getEmployeeID(), employee);
    }

    public void remove(Employee employee) {
        cache.remove(employee.getEmployeeID());
    }

    private void initCache() {
        try (var rs = dbController.searchQuery(TableType.EMPLOYEES)) {
            while (rs.next()) {
                Employee req = new Employee(
                    rs.getObject("employeeID", java.lang.Long.class),
                    rs.getObject("firstName", java.lang.String.class),
                    rs.getObject("lastName", java.lang.String.class));
                add(req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public Optional<Employee> get(java.lang.Long key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Map<java.lang.Long, Employee> get(Employee.Field column, Object value) {
        return get(new Employee.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.lang.Long, Employee> get(Employee.Field[] params, Object[] value) {
        var map = new HashMap<java.lang.Long, Employee>();
        if (params.length != value.length) return map;
        cache.values().forEach(v -> {
            var include = true;
            for (int i = 0; i < params.length; i++)
                include &= Objects.equals(params[i].getValue(v), value[i]);
            if (include)
                map.put(v.getEmployeeID(), v);
        });
        return map;
    }

    @Override
    public Map<java.lang.Long, Employee> getAll() {
        return cache;
    }

    @Override
    public void save(Employee employee) {
        Object[] values = {employee.getEmployeeID(), employee.getFirstName(), employee.getLastName()};
        try {
            dbController.insertQuery(TableType.EMPLOYEES, fields, values);
//            add(employee);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void update(Employee employee, Employee.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.EMPLOYEES, "employeeID", employee.getEmployeeID(), Arrays.stream(params).map(Employee.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(employee)).toArray());
//            update(employee);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(Employee employee) {
        try {
            dbController.deleteQuery(TableType.EMPLOYEES, "employeeID", employee.getEmployeeID());
//            remove(employee);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), TableType.EMPLOYEES.name() + "_update")) {
            var update = (PdbController.DatabaseChangeEvent) evt.getNewValue();
            var data = (Employee) update.data();
            switch (update.action()) {
                case UPDATE -> update(data);
                case DELETE -> delete(data);
                case INSERT -> add(data);
            }
        }
    }
}