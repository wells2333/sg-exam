package com.github.tangyi.api.user.attach;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileUploadContext extends UploadContext {

	private File targetFile;

	boolean deleteTargetFileAfterUploaded;
}
