<h1>HttpClient getting access to the a Spring boot Security OAuth2 app</h1>

<b>@farithjhg</b>

This example is a client to the Spring Boot Security OAuth2 Sample

 http://projects.spring.io/spring-boot/

Normally this example tests using CURL, but I decided to create a HttpClient Rest Client to simulate how one app can call a secured rest services.

<pre>
curl localhost:8080/oauth/token -d "grant_type=password&scope=read&username=greg&password=turnquist" -u foo:bar
</pre>

<ul>
<li>grant_type=password (user credentials will be supplied)</li>
<li>scope=read (read only scope)</li>
<li>username=greg (username checked against user details service)</li>
<li>password=turnquist (password checked against user details service)</li>
<li>-u foo:bar (clientid:secret)</li>
</ul>

Response should be similar to this:
<code>{"access_token":"533de99b-5a0f-4175-8afd-1a64feb952d5","token_type":"bearer","expires_in":43199,"scope":"read"}</code>

With the token value, you can now interrogate the RESTful interface like this:

<pre>
curl -H "Authorization: bearer [access_token]" localhost:8080/flights/1
</pre>

You should then see the pre-loaded data like this:

<pre>
{
     "origin" : "Nashville",
     "destination" : "Dallas",
     "airline" : "Spring Ways",
     "flightNumber" : "OAUTH2",
     "date" : null,
     "traveler" : "Greg Turnquist",
     "_links" : {
         "self" : {
             "href" : "http://localhost:8080/flights/1"
         }
     }
}
</pre>

@author Craig Walls
@author Greg Turnquist

