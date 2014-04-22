package cz.muni.fi.pv168.hotel.servlet;

import cz.muni.fi.pv168.hotel.Main;
import cz.muni.fi.pv168.hotel.Person;
import cz.muni.fi.pv168.hotel.PersonManager;
import cz.muni.fi.pv168.hotel.PersonManagerImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author kurocenko
 */
@WebServlet(urlPatterns = {"/", "/delete/*"})
public class PersonServlet extends HttpServlet {

    private PersonManager personManager = new PersonManagerImpl(Main.getDateSource());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("/delete".equals(request.getServletPath())) {

            // check parameter
            List<String> parameters = Collections.list(request.getParameterNames());
            if (!parameters.contains("personId")) {
                throw new IllegalStateException("Parameter personId not found");
            }

            Long personId = Long.valueOf(request.getParameter("personId"));

            personManager.deletePerson(new Person(personId));
        } else if ("/add".equals(request.getServletPath())) {
            // check parameter
            List<String> parameters = Collections.list(request.getParameterNames());
            if (!parameters.contains("name")) {
                throw new IllegalStateException("Parameter name not found");
            }
            if (!parameters.contains("address")) {
                throw new IllegalStateException("Parameter address not found");
            }
            if (!parameters.contains("phone")) {
                throw new IllegalStateException("Parameter phone not found");
            }

            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");

            Person person = new Person(null, name, phone, address);

            personManager.createPerson(person);
        } else if ("/edit".equals(request.getServletPath())) {
            // check parameter
            List<String> parameters = Collections.list(request.getParameterNames());
            if (!parameters.contains("name")) {
                throw new IllegalStateException("Parameter name not found");
            }
            if (!parameters.contains("address")) {
                throw new IllegalStateException("Parameter address not found");
            }
            if (!parameters.contains("phone")) {
                throw new IllegalStateException("Parameter phone not found");
            }
            if (!parameters.contains("id")) {
                throw new IllegalStateException("Parameter id not found");
            }

            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");

            Person person = new Person(Long.valueOf(id), name, phone, address);

            personManager.updatePerson(person);
        }

        response.sendRedirect(response.encodeRedirectURL("/"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("/".equals(request.getServletPath())) {
            request.setAttribute("people", personManager.findAllPeople());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
        } if ("/edit".equals(request.getServletPath())) {
            request.setAttribute("people", personManager.findAllPeople());
            String id = getParameter(request, "id");
            request.setAttribute("editPerson", personManager.findPersonById(Long.valueOf(id)));
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
        }
    }
    
    private String getParameter(HttpServletRequest request, String parameterName) {
        List<String> parameters = Collections.list(request.getParameterNames());
        if (!parameters.contains(parameterName)) {
            throw new IllegalStateException("Parameter " + parameterName + " not found");
        }
        
        return request.getParameter(parameterName);
    }
}
