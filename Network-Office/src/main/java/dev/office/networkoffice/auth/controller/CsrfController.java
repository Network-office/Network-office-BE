package dev.office.networkoffice.auth.controller;

import dev.office.networkoffice.auth.controller.docs.CsrfApiDocs;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/csrf")
public class CsrfController implements CsrfApiDocs {

    @PostMapping()
    public void refreshCsrfToken() {
    }
}
