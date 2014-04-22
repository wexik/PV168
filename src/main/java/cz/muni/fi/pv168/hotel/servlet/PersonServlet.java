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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mashka
 */
@WebServlet(urlPatterns = {"/"})
public class PersonServlet extends HttpServlet {

    private static final String URL_DELETE = "/delete";
    private static final String URL_ADD = "/add";
    private static final String URL_ROOT = "/";
    private static final String URL_EDIT = "/edit";
    private static final String PARAM_ID = "id";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONE = "phoneNumber";
    private static final String ATTR_PEOPLE = "people";
    private static final String ATTR_EDIT_PERSON = "editPerson";
    private static final String JSP_LIST = "/WEB-INF/jsp/list.jsp";

    private PersonManager personManager = new PersonManagerImpl(Main.getDateSource());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (URL_DELETE.equals(request.getServletPath())) {
            validateParameters(request, PARAM_ID);

            Long personId = getLongParameter(request, PARAM_ID);
            personManager.deletePerson(new Person(personId));
        } else if (URL_ADD.equals(request.getServletPath())) {
            validateParameters(request, PARAM_NAME, PARAM_PHONE, PARAM_ADDRESS);

            Person person = getPersonFromRequest(request);

            personManager.createPerson(person);
        } else if (URL_EDIT.equals(request.getServletPath())) {
            validateParameters(request, PARAM_ID, PARAM_NAME, PARAM_PHONE, PARAM_ADDRESS);

            Person person = getPersonFromRequest(request);
            person.setId(getLongParameter(request, PARAM_ID));

            personManager.updatePerson(person);
        }

        response.sendRedirect(response.encodeRedirectURL(URL_ROOT));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (URL_ROOT.equals(request.getServletPath())) {
            request.setAttribute(ATTR_PEOPLE, personManager.findAllPeople());
            request.getRequestDispatcher(JSP_LIST).forward(request, response);
        } if (URL_EDIT.equals(request.getServletPath())) {
            validateParameters(request, PARAM_ID);

            Long id = getLongParameter(request, PARAM_ID);

            request.setAttribute(ATTR_PEOPLE, personManager.findAllPeople());
            request.setAttribute(ATTR_EDIT_PERSON, personManager.findPersonById(id));

            request.getRequestDispatcher(JSP_LIST).forward(request, response);
        }
    }

    /**
     * Gets all mandatory attributes of Person entity from request
     */
    private Person getPersonFromRequest(HttpServletRequest request) {
        String name = request.getParameter(PARAM_NAME);
        String address = request.getParameter(PARAM_ADDRESS);
        String phone = request.getParameter(PARAM_PHONE);

        return new Person(null, name, phone, address);
    }

    /**
     * Gets parameter from request
     */
    private String getParameter(HttpServletRequest request, String parameterName) {
        List<String> parameters = Collections.list(request.getParameterNames());
        if (!parameters.contains(parameterName)) {
            throw new IllegalStateException("Parameter " + parameterName + " not found");
        }
        
        return request.getParameter(parameterName);
    }

    /**
     * Gets parameter from request and converts to Long is possible
     */
    private Long getLongParameter(HttpServletRequest request, String parameterName) {
        return Long.valueOf(getParameter(request, parameterName));
    }

    /**
     * Checks whether specified parameters exist in request and don't have epty value
     */
    private void validateParameters(HttpServletRequest request, String ... parametersToCheck) {
        List<String> notFoundParameters = getNotFoundParams(request, parametersToCheck);

        if (!notFoundParameters.isEmpty()) {
            throw new IllegalStateException("Following parameters are mandatory and were not found:" + notFoundParameters.toString());
        }
    }

    private List<String> getNotFoundParams(HttpServletRequest request, String... parametersToCheck) {
        List<String> notFoundParameters = new ArrayList<>();

        List<String> availableParameters = Collections.list(request.getParameterNames());
        for (String parameter : parametersToCheck) {
            if (!availableParameters.contains(parameter) || request.getParameter(parameter).trim().isEmpty()) {
                notFoundParameters.add(parameter);
            }
        }

        return notFoundParameters;
    }
}
