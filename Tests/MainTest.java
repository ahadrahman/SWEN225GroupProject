import Application.Main;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * Class for testing main
 * @author Justina
 */
public class MainTest {

    /**
     * Tests a valid move
     */
    @Test
    public void doValidMove() {
        //get class get methods
        Method[]  methodList = getClass().getDeclaredMethods();
        for(Method m :methodList){
            if(m.getName() == "doMove"){
                Class c = m.getDeclaringClass();
            }
        }
    }


}
