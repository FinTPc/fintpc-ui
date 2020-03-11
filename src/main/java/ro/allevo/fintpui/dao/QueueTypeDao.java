package ro.allevo.fintpui.dao;

import org.springframework.stereotype.Service;

import ro.allevo.fintpui.model.QueueType;

@Service
public interface QueueTypeDao {
	public QueueType[] getQueueTypes();
}
