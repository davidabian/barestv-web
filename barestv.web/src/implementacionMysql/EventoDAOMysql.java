package implementacionMysql;

import java.sql.ResultSet;
import java.util.ArrayList;

import beans.*;
import dao.*;
import db.DBFacade;

public class EventoDAOMysql implements EventoInterfazDAO {

    private DBFacade db = null;

    public EventoDAOMysql() throws Exception {
        try {
            db = new DBFacade();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error creando DBDacade");
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ArrayList<Evento> getAll(String establecimiento)
            throws Exception {
        // TODO Auto-generated method stub
        ArrayList<Evento> eventos = null;

        try {

            db.abrirConexion();
            String sql = "select b.nombre nombre,p.titulo titulo, p.bar bar, p.descr descr, p.destacado destacado, p.inicio inicio, p.fin fin, p.cat cat from programa p,bar b  where p.bar= b.nickbar and bar like ?;"; // bar es un acronimo sacarlo de la tabla bar
            System.out.println(sql);
            ResultSet rs = db.ejecutarConsulta(sql, establecimiento);
            eventos = new ArrayList<Evento>();
            while (rs.next()) {

                Evento e = new Evento(
                        //nombre,desc, fechaini,fechafin,categoria
                        rs.getString("titulo"),
                        rs.getString("bar"),
                        rs.getString("descr"),
                        rs.getBoolean("destacado"),
                        rs.getTimestamp("inicio"), rs.getTimestamp("fin"), rs.getString("cat")
                );
                e.setNombrelargo(rs.getString("nombre"));

                eventos.add(e);

            }
        } catch (Exception e) {
            System.out.println("Error al obtener eventos: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            try {

                db.cerrarConexion();
            } catch (Exception e1) {
                System.out.println("Error cerrando la conexi?n");
            }
        }
        return eventos;
    }

    @Override
    public Evento get(String establecimiento, String tituloEvento) throws Exception {
        // TODO Auto-generated method stub
        Evento ev = null;

        try {

            db.abrirConexion();
            String sql = "select * from programa where bar like ? and titulo like ?;"; // bar es un acronimo sacarlo de la tabla bar
            System.out.println(sql);
            ResultSet rs = db.ejecutarConsulta(sql, establecimiento, tituloEvento);

            while (rs.next()) {
                ev = new Evento(
                        //nombre,desc, fechaini,fechafin,categoria
                        rs.getString("titulo"),
                        rs.getString("bar"),
                        rs.getString("descr"),
                        rs.getBoolean("destacado"),
                        rs.getTimestamp("inicio"), rs.getTimestamp("fin"), rs.getString("cat")
                );

            }
        } catch (Exception e) {
            System.out.println("Error al obtener evento: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            try {

                db.cerrarConexion();
            } catch (Exception e1) {
                System.out.println("Error cerrando la conexi?n");
            }
        }
        return ev;
    }

    @Override
    public boolean add(String establecimiento, Evento e) throws Exception {
        // TODO Auto-generated method stub
        Boolean esCorrecto = true;
        try {
            java.text.SimpleDateFormat sdf
                    = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String ini = sdf.format(e.getInicio());
            String fin = sdf.format(e.getFin());

            db.abrirConexion();
            String queryString = "insert into programa (titulo, bar, descr, destacado, inicio, fin, cat) values "
                    + "(?,?,?,'0',?,?,?);";
            db.ejecutarUpdate(queryString, e.getTitulo(), establecimiento, e.getDescr(), ini, fin, e.getCat());
        } catch (Exception ex) {
            System.out.println("Error al insertar evento: " + ex.getMessage());
            esCorrecto = false;
        } finally {
            try {
                db.cerrarConexion();
            } catch (Exception e1) {
                System.out.println("Error cerrando la conexi?n");
                esCorrecto = false;
            }
        }
        return esCorrecto;
    }

    @Override
    public boolean remove(String titulo, String user) throws Exception {
        // TODO Auto-generated method stub
        Boolean esCorrecto = true;
        try {
            db.abrirConexion();
            String queryString = "delete from programa WHERE titulo = ? and bar = ?;";
            db.ejecutarUpdate(queryString, titulo, user);
        } catch (Exception e) {
            System.out.println("Error al eliminar evento: " + e.getMessage());
            esCorrecto = false;
        } finally {
            try {
                db.cerrarConexion();
            } catch (Exception e1) {
                System.out.println("Error cerrando la conexi�n");
                esCorrecto = false;
            }
        }
        return esCorrecto;
    }

    @Override
    public boolean edit(String usuario, Evento e) throws Exception {
        Boolean esCorrecto = true;
        try {
            db.abrirConexion();
            java.text.SimpleDateFormat sdf
                    = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String ini = sdf.format(e.getInicio());
            String fin = sdf.format(e.getFin());
            String queryString = "UPDATE programa "
                    + "SET  descr = ?"
                    + ", destacado = ?"
                    + ", inicio = ?"
                    + ", fin = ?"
                    + ", cat = ?"
                    + " WHERE  titulo = ? and bar = ? ";

            db.ejecutarUpdate(queryString, e.getDescr(), e.getDestacado(), ini, fin, e.getCat(), e.getTitulo(), usuario);
        } catch (Exception ex) {
            System.out.println("Error al modificar establecimiento: " + ex.getMessage());
            esCorrecto = false;
        } finally {
            try {
                db.cerrarConexion();
            } catch (Exception e1) {
                System.out.println("Error cerrando la conexi�n");
                esCorrecto = false;
            }
        }
        return esCorrecto;

    }

    @Override
    public boolean removeAll(String nickbar) throws Exception {
        Boolean esCorrecto = true;
        try {
            db.abrirConexion();
            String queryString = "delete from programa WHERE bar = ?; ";
            db.ejecutarUpdate(queryString, nickbar);
        } catch (Exception e) {
            System.out.println("Error al eliminar eventos: " + e.getMessage());
            esCorrecto = false;
        } finally {
            try {
                db.cerrarConexion();
            } catch (Exception e1) {
                System.out.println("Error cerrando la conexi�n");
                esCorrecto = false;
            }
        }
        return esCorrecto;
    }

    @Override
    public ArrayList<Evento> getAllAll() throws Exception {
        ArrayList<Evento> eventos = null;

        try {

            db.abrirConexion();

            String sql = "select b.nombre nombre,p.titulo titulo, p.bar bar, p.descr descr, p.destacado destacado, p.inicio inicio, p.fin fin, p.cat cat from programa p,bar b  where p.bar= b.nickbar;"; // bar es un acronimo sacarlo de la tabla bar

            System.out.println(sql);
            ResultSet rs = db.ejecutarConsulta(sql);
            eventos = new ArrayList<Evento>();
            while (rs.next()) {

                Evento e = new Evento(
                        //nombre,desc, fechaini,fechafin,categoria
                        rs.getString("titulo"),
                        rs.getString("bar"),
                        rs.getString("descr"),
                        rs.getBoolean("destacado"),
                        rs.getTimestamp("inicio"), rs.getTimestamp("fin"), rs.getString("cat")
                );

                e.setNombrelargo(rs.getString("nombre"));
                eventos.add(e);

            }
        } catch (Exception e) {
            System.out.println("Error al obtener eventos all: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            try {

                db.cerrarConexion();
            } catch (Exception e1) {
                System.out.println("Error cerrando la conexi?n");
            }
        }
        return eventos;
    }

    @Override
    public boolean destacar(String titulo, String bar) throws Exception {
        Boolean esCorrecto = true;
        try {
            db.abrirConexion();
            String queryString = "UPDATE programa "
                    + "SET  destacado = 1"
                    + " WHERE  titulo = ? and bar = ? ";

            db.ejecutarUpdate(queryString, titulo, bar);
        } catch (Exception ex) {
            System.out.println("Error al destacar: " + ex.getMessage());
            esCorrecto = false;
        } finally {
            try {
                db.cerrarConexion();
            } catch (Exception e1) {
                System.out.println("Error cerrando la conexi�n");
                esCorrecto = false;
            }
        }
        return esCorrecto;
    }

    @Override
    public boolean nodestacar(String titulo, String bar) throws Exception {
        Boolean esCorrecto = true;
        try {
            db.abrirConexion();
            String queryString = "UPDATE programa "
                    + "SET  destacado = 0"
                    + " WHERE  titulo = ? and bar = ? ";

            db.ejecutarUpdate(queryString, titulo, bar);
        } catch (Exception ex) {
            System.out.println("Error al nodestacar: " + ex.getMessage());
            esCorrecto = false;
        } finally {
            try {
                db.cerrarConexion();
            } catch (Exception e1) {
                System.out.println("Error cerrando la conexi�n");
                esCorrecto = false;
            }
        }
        return esCorrecto;
    }

}
