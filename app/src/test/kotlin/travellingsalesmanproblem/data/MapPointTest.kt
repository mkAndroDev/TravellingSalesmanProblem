package travellingsalesmanproblem.data

import com.google.android.gms.maps.model.LatLng
import com.krawczyk.maciej.travellingsalesmanproblem.data.MapPoint
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Created by maciejkrawczyk on 10.06.2017.
 */
class MapPointTest {

    @Test
    fun shouldMapPointBeCreatedProperly() {
        /* Given */
        val mapPoint = getPreparedMapPoint()
        /* When */

        /* Then */
        assertTrue { mapPoint.name == "Test point" }
        assertTrue { mapPoint.latLng == LatLng(1.2, 3.4) }
    }

    @Test
    fun shouldMapPointToStringWorksProperly() {
        /* Given */
        val mapPoint = getPreparedMapPoint()
        /* When */

        /* Then */
        val preparedString = "" + mapPoint.latLng.latitude + "," + mapPoint.latLng.longitude

        assertTrue { mapPoint.toString() == preparedString }
    }

    fun getPreparedMapPoint(): MapPoint {
        return MapPoint("Test point", LatLng(1.2, 3.4))
    }

}
