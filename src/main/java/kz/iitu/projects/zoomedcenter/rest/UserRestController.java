package kz.iitu.projects.zoomedcenter.rest;

import javax.validation.Valid;

import kz.iitu.projects.zoomedcenter.model.User;
import kz.iitu.projects.zoomedcenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PreAuthorize( "hasRole(@roles.ADMIN)" )
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> addOwner(@RequestBody @Valid User user, BindingResult bindingResult) throws Exception {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (user == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<User>(user, headers, HttpStatus.BAD_REQUEST);
        }

        this.userService.saveUser(user);
        return new ResponseEntity<User>(user, headers, HttpStatus.CREATED);
    }
}
