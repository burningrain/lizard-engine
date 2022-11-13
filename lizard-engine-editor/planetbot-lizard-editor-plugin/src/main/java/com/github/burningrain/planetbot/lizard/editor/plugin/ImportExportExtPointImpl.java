package com.github.burningrain.planetbot.lizard.editor.plugin;

import com.github.br.starmarines.game.api.galaxy.Planet;
import com.github.br.starmarines.game.api.galaxy.PlanetType;
import com.github.br.starmarines.gamecore.api.Galaxy;
import com.github.burningrain.lizard.editor.api.LizardPluginApi;
import com.github.burningrain.lizard.editor.api.ext.ImportExportExtPoint;
import com.github.burningrain.lizard.engine.api.data.NodeData;
import com.github.burningrain.lizard.engine.api.data.ProcessData;
import com.github.burningrain.lizard.engine.api.data.TransitionData;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import org.pf4j.Extension;

import com.github.br.starmarines.map.ZipMapConverter;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import static com.github.burningrain.planetbot.lizard.editor.plugin.io.Constants.*;

@Extension
public class ImportExportExtPointImpl implements ImportExportExtPoint {


    private final ZipMapConverter mapConverter = new ZipMapConverter();

    @Override
    public String getExtension() {
        return "pb";
    }

    @Override
    public byte[] write(LizardPluginApi pluginApi, ProcessData processData) {
        Galaxy.Builder builder = new Galaxy.Builder(processData.getTitle(), convertImageToBytes(pluginApi.takeMinimapSnapshot()));
        for (NodeData element : processData.getElements()) {
            builder.addPlanet(createPlanet(element), Boolean.parseBoolean(element.getAttributes().get(IS_START_POINT)));
        }

        for (TransitionData transition : processData.getTransitions()) {
            builder.addEdge((short) transition.getSourceId(), (short) transition.getTargetId());
        }
        builder.maxStepsCount(Integer.parseInt(processData.getAttributes().get(MAX_STEPS_COUNT)));

        return mapConverter.toByteArray(builder.build());
    }

    @Override
    public ProcessData read(LizardPluginApi pluginApi, String name, byte[] bytes) {
        Galaxy galaxy = mapConverter.toGalaxy(name, bytes);

        ArrayList<NodeData> elements = new ArrayList<>();
        Collection<Planet> startPoints = galaxy.getStartPoints();
        for (Planet planet : galaxy.getPlanets()) {
            elements.add(createNodeData(planet, startPoints.contains(planet)));
        }

        ArrayList<TransitionData> transitions = new ArrayList<>();
        int i = elements.size() + 1; // fixme нужно ли прибавлять + 1?
        for (Galaxy.Edge edge : galaxy.getEdges()) {
            transitions.add(createEdgeData(i, edge));
            i++;
        }

        HashMap<String, String> attributes = new HashMap<>();
        attributes.put(MAX_STEPS_COUNT, String.valueOf(galaxy.getMaxStepsCount()));

        return new ProcessData(
                galaxy.getTitle(),
                "",
                attributes,
                elements,
                transitions,
                -1
        );
    }

    private NodeData createNodeData(Planet planet, boolean isStartPoint) {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put(IS_START_POINT, String.valueOf(isStartPoint));
        attributes.put(PLANET_TYPE, String.valueOf(planet.getType().getCode()));
        attributes.put(UNITS, String.valueOf(planet.getUnits()));
        attributes.put(OWNER_ID, String.valueOf(planet.getOwnerId()));
        attributes.put(X, String.valueOf(planet.getX()));
        attributes.put(Y, String.valueOf(planet.getY()));

        return new NodeData(
                planet.getId(),   // int id,
                PLUGIN_ID,        // String pluginId,
                VERTEX_TITLE,     // String title,
                attributes,       // Map<String, String> attributes,
                planet.getX(),
                planet.getY(),
                "",      // String className,
                ""                // String description
        );
    }

    private Planet createPlanet(NodeData element) {
        Map<String, String> attributes = element.getAttributes();

        Planet planet = new Planet();
        planet.setX(element.getX());
        planet.setY(element.getY());
        planet.setId((short) element.getId());
        planet.setOwnerId(Short.parseShort(attributes.get(OWNER_ID)));
        planet.setUnits(Integer.parseInt(attributes.get(UNITS)));
        planet.setType(PlanetType.getByCode(Byte.parseByte(attributes.get(PLANET_TYPE))));

        return planet;
    }

    private TransitionData createEdgeData(int id, Galaxy.Edge edge) {
        return new TransitionData(
                id, // int id,
                PLUGIN_ID, // String pluginId,
                EDGE_TITLE, // String title,
                "", // String description,
                Collections.emptyMap(), // Map<String, String> attributes,
                edge.getFrom().getId(), // int sourceId,
                edge.getTo().getId(), // int targetId,
                "" // String tag
        );
    }

    private byte[] convertImageToBytes(Image img) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(img, null);
        try {
            ImageIO.write(renderedImage, "png", out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

}
