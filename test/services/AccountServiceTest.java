package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import scala.xml.factory.NodeFactory;

import static org.junit.Assert.*;

/**
 * Created by Lenny on 25.10.2016.
 */
public class AccountServiceTest {
    AccountService _accountService;
    ObjectNode _user;

    @Before
    public void setUp() throws Exception {
        _accountService = new AccountService();
        _user = new JsonNodeFactory(false).objectNode();
        _user.put("fullname", "Vilhjalmur Alex");
        _user.put("username", "vilhjalmur14");
        _user.put("email", "vilhjalmur14@ru.is");
        _user.put("password", "villi");
        _accountService.createUser(_user);

    }

    @Test
    public void createNonExistingUser() throws Exception {
        //_accountService.createUser(_user);
        ObjectNode user = new JsonNodeFactory(false).objectNode();
        user.put("fullname", "Hoskuldur agustsson");
        user.put("username", "hoskuldur14");
        user.put("email", "hoskuldur14@ru.is");
        user.put("password", "hossi");
        assertEquals(true, _accountService.createUser(user));
        _accountService.deleteUser(user.get("username").toString());
    }

    @Test
    public void createExistingUser() throws Exception {
        assertEquals(false, _accountService.createUser(_user));
    }

    @Test
    public void authenticateValidUser() throws Exception {
        ObjectNode user = new JsonNodeFactory(false).objectNode();
        user.put("username", "vilhjalmur14");
        user.put("password", "villi");
        _accountService.authenticateUser(user);
    }

    @Test (expected=ServiceException.class)
    public void authenticateInvalidUser() throws Exception {
        ObjectNode user = new JsonNodeFactory(false).objectNode();
        user.put("username", "vilhjalmur14");
        user.put("password", "ekkivilli");
        _accountService.authenticateUser(user);
    }

    @After
    public void tearDown() throws Exception {
        _accountService.deleteUser(_user.get("username").toString());
    }

}