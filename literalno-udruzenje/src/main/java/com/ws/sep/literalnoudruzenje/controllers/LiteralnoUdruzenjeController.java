package com.ws.sep.literalnoudruzenje.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping( "lu" )
public class LiteralnoUdruzenjeController
{
        @GetMapping( value = "/bank" )
        public ResponseEntity< String > responseBank()
        {
//                RestTemplate restTemplate = new RestTemplate();
//                String fooResourceUrl = "https://localhost:8765/bank-service/test1/";
//                ResponseEntity< String > response = restTemplate.getForEntity( fooResourceUrl, String.class );
//                // assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
                return ResponseEntity.ok("bank");

        }


        @GetMapping( value = "/bitcoin" )
        public ResponseEntity< String > responseBitcoin()
        {
                RestTemplate restTemplate = new RestTemplate();
                String fooResourceUrl = "https://localhost:8765/bitcoin-service/test1/";
                ResponseEntity< String > response = restTemplate.getForEntity( fooResourceUrl, String.class );
                return response;

        }

}
