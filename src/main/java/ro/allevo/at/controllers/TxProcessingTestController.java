package ro.allevo.at.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.allevo.at.model.InputDataset;
import ro.allevo.at.model.TxProcessingTest;
import ro.allevo.at.model.TxProcessingTestLog;
import ro.allevo.at.service.InputDatasetService;
import ro.allevo.at.service.TxProcessingTestLogService;
import ro.allevo.at.service.TxProcessingTestService;
import ro.allevo.fintpui.config.Config;

@Controller
@RequestMapping("tests")
public class TxProcessingTestController {

	@Autowired
	Config config;

	@Autowired
	private TxProcessingTestService txProcessingTestService;
	
	@Autowired
	private InputDatasetService inputDatasetService;
	
	@Autowired
	private TxProcessingTestLogService txProcessingTestLogService;
	
	private TxProcessingTest[] txProcessingTests;
	
	/*
	 * DISPLAY
	 */
	@GetMapping
	public String displayTests(ModelMap model) {

		txProcessingTests = txProcessingTestService.getAllProcessingTests();
		model.addAttribute("txProcessingTests" , txProcessingTests);
		
		Map<String, Integer> uniqueNameMap = new HashMap<String, Integer>();
		for(TxProcessingTest txProcessingTest: txProcessingTests) {
//			if(uniqueNameMap.get(txProcessingTest.getName()) != null) {
				uniqueNameMap.put(txProcessingTest.getName(), txProcessingTest.getId().intValue());
//			}
		}

		model.addAttribute("uniqueNameMap", uniqueNameMap);
		
		return "at/processingTests";
	}
	
	@GetMapping(path = "{id}/transaction-type")
	@ResponseBody
	public Map<Integer, String> getTxProcessingTestType(@PathVariable int id) {

		// search in txProcessingTests by id, for each same name get txtype and display it
		String txProcessingTestName = null;
		for(TxProcessingTest txProcessingTest: txProcessingTests) {
			if(txProcessingTest.getId() == id) {
				txProcessingTestName = txProcessingTest.getName();				
			}
		}
		
		// when find "txProcessingTestName"  -- can find out all Txtypes		
		Map<Integer, String> txTypes = new HashMap<Integer, String>();		
		for(TxProcessingTest txProcessingTest: txProcessingTests) {
			if(txProcessingTest.getName().equals(txProcessingTestName)) {
				txTypes.put(txProcessingTest.getInterfaceconfig().getId(), txProcessingTest.getTxtype());
			}
		}
		
//		return map (key = id (which is interfaceId), value = txtype)
		return txTypes;
	}
	
	@GetMapping(path = "{id}/input-dataset-type")
	@ResponseBody
	public Map<String, String> getinputDataSetType(@PathVariable int id) {
		
		Map<String, String> inputDataSetTypes = new HashMap<String, String>();

		// search all datasettype from table inputdatasets by interfaceId and display them in select	
		InputDataset[] inputDataSets = inputDatasetService.getAllInputDatasetsById((long) id);
		for(InputDataset inputDataset : inputDataSets) {
			// pass datasettype and id with separator :   as value
			inputDataSetTypes.put(inputDataset.getDataset(), inputDataset.getDatasettype() + ':' + inputDataset.getId());
		}

		// return map with (key = name of file = dataset) and value = datasettype
		return inputDataSetTypes;
	}     
	
	@GetMapping(path = "download-file/{filename}")
	@ResponseBody
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String fileName) throws IOException{
		
		Path path = Paths.get("C://AT Repository/" + fileName);		
		
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
		
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        
		return ResponseEntity.ok()
	            .headers(headers)
	            .contentType(MediaType.parseMediaType("application/octet-stream"))
	            .body(resource);
	}

	@GetMapping(path = "{inputType}/{inputDataSetFileName}/{txProcessingTestId}/{inputDatasetId}")
	@ResponseBody
	public String copyFileToAnotherLocation(@PathVariable("inputType") String inputType, @PathVariable("inputDataSetFileName") String fileName,
					@PathVariable("txProcessingTestId") long txProcessingTestId, @PathVariable("inputDatasetId") long inputDatasetId) {
		
		String resultOutput;
		
		if(inputType.equalsIgnoreCase("file")) {
			for(File file: new java.io.File("C://AT Repository output").listFiles()) {
				if (!file.isDirectory()) {
					file.delete();
				}
			}
				
			Path originalPath = Paths.get("C://AT Repository/" + fileName);
		    Path copied = Paths.get("C://AT Repository output/" + fileName);
		    
		    try {
				Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
				resultOutput = "File copied successfully";
			} catch (IOException e) {
				resultOutput = "Error copy file";
				e.printStackTrace();
			}						
			
			// sleep n sec
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
//			insertTestLog(txProcessingTestId, inputDatasetId);
		}else {
			// pass this to ajax
			resultOutput = "DB";
		}		
		
		return resultOutput;
	}
	
	private void insertTestLog(long txProcessingTestId, long inputDatasetId) {	
		
		InputDataset inputDataset = null;

		for(InputDataset inputDatasetObject : inputDatasetService.getAllInputDatasets()) {
			if(inputDatasetObject.getId() == inputDatasetId) {
				inputDataset = inputDatasetObject;
				break;
			}
		}
		
		// or ---- have to implement one of them
		inputDataset = inputDatasetService.getInputDataset(inputDatasetId);
		
		TxProcessingTest txProcessingTest = null;
		
		for(TxProcessingTest processingTest : txProcessingTests) {
			if(processingTest.getId() == txProcessingTestId) {
				txProcessingTest = processingTest;
				break;
			}
		}
		
		TxProcessingTestLog txProcessingTestLog = new TxProcessingTestLog();
		txProcessingTestLog.setInputdataset(inputDataset);
		txProcessingTestLog.setInsertdate(getCurrentTime());
		txProcessingTestLog.setStatus(0);
		
		txProcessingTestLog.setTxprocessingtest(txProcessingTest);
		txProcessingTestLog.setTxtype(txProcessingTest.getTxtype());
		txProcessingTestLogService.insertTxProcessingTestLog(txProcessingTestLog);
	}
	
	private Timestamp getCurrentTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return java.sql.Timestamp.from( localDateTime.toInstant(ZoneId.systemDefault().getRules().getOffset(localDateTime)) );
	}
	
//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//	Calendar cal = Calendar.getInstance();
//	cal.add(Calendar.HOUR_OF_DAY, -1);
//	String date  = sdf.format(cal.getTime());
	
	
	
	// ar mai trebui creata o metoda care sa insereze in finat.txprocessingtestlog ---> fie dupa sleep, fie dupa ce se apasa pe btn din UI de Confirm DB insert
	
	// get entity de inmplementat
//	@Path(value="{id}")
//	public TxProcessingTest getTxProcessingTest(@RequestParam long id) {
//		TxProcessingTest result = txProcessingTestService.getProcessingTest(id);
//		return result;
//	}
	
//	@GetMapping
//	@ResponseBody
//	public String getTimestamps(@RequestParam("date") Date date) throws JsonProcessingException{
//		logger.info("live get intervals requested");
//		
//		return JSONHelper.toString(overallService.getTimestamps(date));
//	}
}
