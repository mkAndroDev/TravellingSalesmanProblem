package travellingsalesmanproblem.data

import com.krawczyk.maciej.travellingsalesmanproblem.data.AdjacencyPoint
import com.krawczyk.maciej.travellingsalesmanproblem.data.GraphPoint
import com.krawczyk.maciej.travellingsalesmanproblem.data.Route
import io.realm.RealmList
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Created by maciejkrawczyk on 10.06.2017.
 */
class RouteTest {

    @Test
    fun shouldRouteBeCreatedProperly() {
        /* Given */
        val route = getPreparedRoute()
        /* When */

        /* Then */
        assertTrue { route.distances[2] == 3 }
        assertTrue { route.points[2].name == "Test point3" }
    }

    @Test
    fun shouldRouteContainsPoint() {
        /* Given */
        val route = getPreparedRoute()
        /* When */

        /* Then */
        assertTrue { route.containsPoint(1.2, 3.4) }
    }

    @Test
    fun shouldRouteDistanceBeProperly() {
        /* Given */
        val route = getPreparedRoute()
        /* When */

        /* Then */
        assertTrue { route.distanceForRoute == 15 }
    }

    fun getPreparedRoute(): Route {
        val route = Route()
        for (i: Int in 1..5) {
            route.addDistance(i)
            route.addPoint(GraphPoint("Test point" + i, 1.2, 3.4, RealmList<AdjacencyPoint>()))
        }
        return route
    }

}
