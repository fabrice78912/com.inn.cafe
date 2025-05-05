package com.inn.cafe.serviceImpl;

class UserServiceImplTest {

   /* private AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private CustomerUsersDetailsService customerUsersDetailsService = mock(CustomerUsersDetailsService.class);
    private JwtUtils jwtUtils = mock(JwtUtils.class);
    private LoginFunction loginFunction = new LoginFunction(authenticationManager, customerUsersDetailsService, jwtUtils);

    @Test
    public void testSuccessfulLogin() {
        // Mock request parameters
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", "test@example.com");
        requestMap.put("password", "password123");

        // Mock authentication result
        Authentication mockedAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockedAuth);
        when(mockedAuth.isAuthenticated()).thenReturn(true);

        // Mock user details
        UserDetails userDetails = new UserDetails("test@example.com", "ROLE_USER", "true");
        when(customerUsersDetailsService.getUserDetail()).thenReturn(userDetails);

        // Mock JWT token generation
        when(jwtUtils.generatedToken(eq("test@example.com"), eq("ROLE_USER"))).thenReturn("mocked_token");

        // Perform the login
        ResponseEntity<String> response = loginFunction.login(requestMap);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"token\":\"mocked_token\"}", response.getBody());
    }

    @Test
    public void testUnauthenticatedLogin() {
        // Similar setup as above, but mocking unauthenticated scenario

        // ...

        // Mock authentication failure
        Authentication mockedAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockedAuth);
        when(mockedAuth.isAuthenticated()).thenReturn(false);

        // Perform the login
        ResponseEntity<String> response = loginFunction.login(requestMap);

        // Assertions for unauthenticated scenario
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        // Add more assertions as needed
    }

*/

}