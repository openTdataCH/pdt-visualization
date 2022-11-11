package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.opentdata.wsdl.MeasurementSiteRecord;
import ch.opentdata.wsdl.Point;
import ch.opentdata.wsdl.PointByCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for Measurement points
 *
 * @author Sven Trachsel
 */
@Service
public class MeasurementPointServiceImpl implements MeasurementPointService {

	private final MeasurementPointRepository measurementPointRepository;
	private final DtoMapper dtoMapper;

	@Autowired
	public MeasurementPointServiceImpl(DtoMapper dtoMapper, MeasurementPointRepository measurementPointRepository) {
		this.measurementPointRepository = measurementPointRepository;
		this.dtoMapper = dtoMapper;
	}

	@Override
	public void processAndPersistMeasurementPoints(List<MeasurementSiteRecord> measurementSiteRecords) {
		final List<MeasurementPoint> measurementPoints = new ArrayList<>();
		for (MeasurementSiteRecord mSRecord : measurementSiteRecords) {
			boolean isActive = false;
			if (!(mSRecord.getMeasurementSiteLocation() instanceof final Point pt)) {
				throw new ClassCastException("Expected Point, but was not");
			}

			final double[] coordinates = {0, 0};
			// extract coordinates
			final PointByCoordinates ptByCoords = pt.getPointByCoordinates();
			if (ptByCoords != null) {
				isActive = true;
				coordinates[0] = ptByCoords.getPointCoordinates().getLatitude();
				coordinates[1] = ptByCoords.getPointCoordinates().getLongitude();
			}
			measurementPoints.add(
					MeasurementPoint.builder()
							.id(mSRecord.getId())
							.latitude(coordinates[0])
							.longtitude(coordinates[1])
							.numberOfLanes(mSRecord.getMeasurementSiteNumberOfLanes().intValue())
							.active(isActive)
							.build()
			);
		}
		measurementPointRepository.saveAll(measurementPoints);
	}

	@Override
	public GeoJsonFeatureCollectionDto getAllMeasurementPointsGeoJson() {
		final List<MeasurementPoint> measurementPoints = measurementPointRepository.findAllByActive(true);
		return dtoMapper.mapMeasurementPointsToGeoJsonFeatureCollectionDto(measurementPoints);
	}

	@Override
	public int getNumberOfMeasurementPoints() {
		return measurementPointRepository.countAllByActive(true);
	}

}
