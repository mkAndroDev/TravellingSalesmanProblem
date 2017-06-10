package travellingsalesmanproblem.data

import com.google.android.gms.maps.model.LatLng
import com.krawczyk.maciej.travellingsalesmanproblem.data.AdjacencyPoint
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Created by maciejkrawczyk on 10.06.2017.
 */
class AdjacencyPointTest {

    @Test
    fun shouldBeAdjacencyPointCreatedProperly() {
        /* Given */
        val adjacencyPoint = getPreparedAdjacencyPoint()
        /* When */

        /* Then */
        assertTrue { adjacencyPoint.pointStartLat == 1.2 }
        assertTrue { adjacencyPoint.pointStartLon == 3.4 }
        assertTrue { adjacencyPoint.pointEndLat == 5.6 }
        assertTrue { adjacencyPoint.pointEndLon == 7.8 }
        assertTrue { adjacencyPoint.weight == 9 }
    }

    @Test
    fun shouldAdjacencyPointToStringWorksProperly() {
        /* Given */
        val adjacencyPoint = getPreparedAdjacencyPoint()
        /* When */

        /* Then */
        val preparedString = "" + adjacencyPoint.pointStartLat + ", " + adjacencyPoint.pointStartLon + " : " + adjacencyPoint.pointEndLat + ", " + adjacencyPoint.pointEndLon + " -> " + adjacencyPoint.weight

        assertTrue { adjacencyPoint.toString() == preparedString }
    }

    fun getPreparedAdjacencyPoint(): AdjacencyPoint {
        return AdjacencyPoint(LatLng(1.2, 3.4), LatLng(5.6, 7.8), 9)
    }

}
