import com.example.indiproject.DTOs.LoginDTO
import com.example.indiproject.models.AppUser
import com.example.indiproject.repositories.AppUserRepository
import com.example.indiproject.services.api.AppUserService
import com.example.indiproject.services.impl.AppUserServiceImpl
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class AppUserServiceImplTest extends Specification{
    AppUserRepository appUserRepository = Mock()

    @Subject
    AppUserService appUserService = new AppUserServiceImpl(appUserRepository)

    @Unroll
    def "Should check if the user exists"() {
        given:
        appUserRepository.findByUsername("Kuba007") >> new AppUser("Kuba007","123",0.5);
        when:
        def result = appUserService.appUserWithUserNameExists(name);
        then:
        result == expectedResult
        where:
        name || expectedResult
        "Kuba007" || true
        "Martin" || false
    }

    @Unroll
    def "Should check if the username is appropriate"() {
        when:
        def result = appUserService.appUserNameIsAppropriate(name)
        then:
        result == expectedResult
        where:
        name || expectedResult
        "FuckKuba" || false
        "KubaIsShit" || false
        "Kuba007" || true
    }

    @Unroll
    def "Should check if the password matcher username"() {
        given:
        appUserRepository.findByUsername("Kuba007") >> new AppUser("Kuba007","123",0.5);
        when:
        def result = appUserService.passwordMatchesAppUserName(login);
        then:
        result == expectedResult
        where:
        login || expectedResult
        new LoginDTO("Kuba007","123") || true
        new LoginDTO("Kuba007","abc") || false
    }

    @Unroll
    def "Should return user details"() {
        given:
        def name = "Kuba007"
        appUserRepository.findByUsername("Kuba007") >> new AppUser("Kuba007","123",0.5);
        when:
        def result = appUserService.loadUserByUsername(name);
        then:
        result == new User("Kuba007", "123", new ArrayList<>())
    }

    @Unroll
    def "Should return exception \"User not found\"" () {
        given:
        def name = "Martin"
        appUserRepository.findByUsername("Kuba007") >> new AppUser("Kuba007","123",0.5);
        when:
        appUserService.loadUserByUsername(name);
        then:
        def e = thrown(UsernameNotFoundException)
        e.message == "User not found"
    }

    @Unroll
    def "Should return user id"() {
        given:
        def appUser = new AppUser("Kuba007","123",0.5)
        appUser.setId(1);
        appUserRepository.findByUsername("Kuba007") >> appUser;
        when:
        def result = appUserService.getIdByUsername("Kuba007");
        then:
        result == 1
    }

    @Unroll
    def "Should return null pointer exception"() {
        given:
        def appUser = new AppUser("Kuba007","123",0.5)
        appUser.setId(1);
        appUserRepository.findByUsername("Kuba007") >> appUser;
        when:
        appUserService.getIdByUsername("Martin");
        then:
        thrown(NullPointerException)
    }
}