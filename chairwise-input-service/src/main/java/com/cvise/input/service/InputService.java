package com.cvise.input.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cvise.input.entity.Input;
import com.cvise.input.exception.FileReadException;
import com.cvise.input.exception.FileStorageException;
import com.cvise.input.exception.NotFoundException;
import com.cvise.input.repository.InputRepository;
import com.cvise.input.repository.InputRepositoryCustomImpl;

@Service
public class InputService {
	@Value("${user.home}")
    public String localFilesReferenceDir;
    @Value("${user.local.static.file.path}")
	private String staticFilePath;
	@Value("${user.local.static.file.endpoint}")
	private String staticFileEndpoint;
    
    private InputRepository repository;
    private InputRepositoryCustomImpl customRepository;

    public InputService(InputRepository inputRepository,
    					InputRepositoryCustomImpl customInputRepository) {
        this.repository = inputRepository;
       	this.customRepository = customInputRepository;
    }
       
    public List<String> getInputById(String id) {
    	Input input = repository.findById(id).orElseThrow(NotFoundException::new);
    	return input.getCorpus();
    }
    
    public String saveInputToFileById(String id, HttpServletRequest request) {
    	String baseUrl = getStaticFilesUrlFromRequest(request);
    	List<String> inputCorpus = this.getInputById(id);
    	String fileName = UUID.randomUUID().toString() + ".txt";
//		String filePath = uploadDir + File.separator + fileName;
		String filePath = staticFilePath + fileName;
    	Path fileLocation = Paths.get(filePath);
    	try {
	    	Files.write(fileLocation, inputCorpus);
	    	return baseUrl + fileName;
    	} catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not prepare file of input id "
            	+ id 
            	+ " for download at "
            	+ fileLocation.getFileName()
                + ". Please try again!");
        }
    }
    
	public String saveInput(String text, HttpServletRequest request) {
		String baseUrl = getStaticFilesUrlFromRequest(request);
		String databaseId = saveTextToDatabase(text);
		return baseUrl + databaseId; 
	}
	
	public String saveInput(List<String> textList, HttpServletRequest request) {
		String baseUrl = getStaticFilesUrlFromRequest(request);
		String databaseId = saveTextListToDatabase(textList);
		return baseUrl + databaseId; 
	}
	
	public String saveInput(MultipartFile file, HttpServletRequest request) {
		String baseUrl = getStaticFilesUrlFromRequest(request);
		Path localFilePath = uploadFile(file);
		String databaseId = saveFileToDatabase(localFilePath);
		return baseUrl + databaseId; 
	}
	
	public String saveTextToDatabase(String text) {
		Input newInput = repository.save(new Input(new ArrayList<String>()));
		String inputId = newInput.getId();
		String lines[] = text.split("\\r?\\n");
		for (String line : lines) {
			customRepository.pushUniqueCorpusById(inputId, line);
		}
		return inputId;
	}
	
	public String saveTextListToDatabase(List<String> textList) {
		System.out.println(textList);
		Input newInput = repository.save(new Input(new ArrayList<String>()));
		String inputId = newInput.getId();
		for (String line : textList) {
			customRepository.pushUniqueCorpusById(inputId, line);
		}
		return inputId;
	}
	
	public String saveFileToDatabase(Path localFilePath) {
		try (Stream<String> stream = Files.lines(localFilePath)) {
			Input newInput = repository.save(new Input(new ArrayList<String>()));
			String inputId = newInput.getId();
			stream.forEach((line -> {
				customRepository.pushUniqueCorpusById(inputId, line);
			}));
			Files.deleteIfExists(localFilePath);
			return inputId;
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileReadException("Could not store file "
				+ localFilePath.getFileName()
                + " into database. Please try again!");
		}
	}
	
	public Path uploadFile(MultipartFile file) {
		try {
			String fileName = UUID.randomUUID().toString() + ".txt";
			String filePath = localFilesReferenceDir + File.separator + fileName;
        	Path copyLocation = Paths.get(filePath);
        	Files.copy(file.getInputStream(),
        			   copyLocation,
        			   StandardCopyOption.REPLACE_EXISTING);		 
        	return copyLocation;    
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file "
            	+ file.getOriginalFilename()
                + ". Please try again!");
        }
	}
	
	public String getFileStreamUrlFromRequest(HttpServletRequest request) {
		return String.format("%s://%s:%d/api/v1/Input/Stream/",
			request.getScheme(),
			request.getServerName(),
			request.getServerPort());
	}
	
	public String getStaticFilesUrlFromRequest(HttpServletRequest request) {
		return String.format("%s://%s:%d%s",
			request.getScheme(),
			request.getServerName(),
			request.getServerPort(),
			staticFileEndpoint);
	}
}
