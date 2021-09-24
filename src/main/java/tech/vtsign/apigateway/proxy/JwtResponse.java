package tech.vtsign.apigateway.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class JwtResponse {

    private String jwttoken;

}
