package travellingsalesmanproblem.data

import com.krawczyk.maciej.travellingsalesmanproblem.data.AdjacencyPoint
import com.krawczyk.maciej.travellingsalesmanproblem.data.GraphPoint
import io.realm.RealmList
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Created by maciejkrawczyk on 10.06.2017.
 */
class GraphPointTest {

    @Test
    fun shouldGraphPointBeCreatedProperly() {
        /* Given */
        val graphPoint = getPreparedGraphPoint()
        /* When */

        /* Then */
        assertTrue { graphPoint.lat == 1.2 }
        assertTrue { graphPoint.lon == 3.4 }
        assertTrue { graphPoint.name == "Test point" }
    }

    @Test
    fun shouldGraphPointToStringWorksProperly() {
        /* Given */
        val graphPoint = getPreparedGraphPoint()
        /* When */

        /* Then */
        val preparedString = "" + graphPoint.lat + ", " + graphPoint.lon

        assertTrue { graphPoint.toString() == preparedString }
    }

    fun getPreparedGraphPoint(): GraphPoint {
        return GraphPoint("Test point", 1.2, 3.4, RealmList<AdjacencyPoint>())
    }

}
