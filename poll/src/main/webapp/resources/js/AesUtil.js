var AesUtil = function(keySize, iterationCount) {
	this.keySize = keySize / 32;
	this.iterationCount = iterationCount;
};

AesUtil.prototype.generateKey = function(salt, passPhrase) {
	var key = CryptoJS.PBKDF2(passPhrase, CryptoJS.enc.Hex.parse(salt), {
		keySize : this.keySize,
		iterations : this.iterationCount
	});
	return key;
}

AesUtil.prototype.encrypt = function(salt, iv, passPhrase, plainText) {
	var key = this.generateKey(salt, passPhrase);
	var encrypted = CryptoJS.AES.encrypt(plainText, key, {
		iv : CryptoJS.enc.Hex.parse(iv)
	});
	return encrypted.ciphertext.toString(CryptoJS.enc.Base64);
}

AesUtil.prototype.decrypt = function(salt, iv, passPhrase, cipherText) {
	var key = this.generateKey(salt, passPhrase);
	var cipherParams = CryptoJS.lib.CipherParams.create({
		ciphertext : CryptoJS.enc.Base64.parse(cipherText)
	});
	var decrypted = CryptoJS.AES.decrypt(cipherParams, key, {
		iv : CryptoJS.enc.Hex.parse(iv)
	});
	return decrypted.toString(CryptoJS.enc.Utf8);
}

AesUtil.prototype.customEncrypt = function(plainText) {
	var iv = "fc7a1fef51b93ca99915a67f1936cd5c";
	var salt = "45840955a57622ff39dce3bdbab38394";
	var passPhrase = "reyon#1031";
	return this.encrypt(salt, iv, passPhrase, plainText);
}

AesUtil.prototype.customDecrypt = function(cipherText) {
	var iv = "fc7a1fef51b93ca99915a67f1936cd5c";
	var salt = "45840955a57622ff39dce3bdbab38394";
	var passPhrase = "reyon#1031";
	return this.decrypt(salt, iv, passPhrase, cipherText);
}