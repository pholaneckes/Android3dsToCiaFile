package love.digimons.f3dstocia;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import com.google.common.primitives.Bytes;

public class File3dsTocia {
    public static byte[] ciainfo = "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".getBytes();
    public static final int READ_SIZE = 8 * 1024 * 1024;
	public static RandomAccessFile rom;
	public static RandomAccessFile cia;
	public static int game_cxi_size;
	public static MessageDigest game_cxi_hash;
	public static byte[] ncch_header;
	public static byte[] extheader;
    public static void show_progress(int val, int maxval, Context ct) {
        int minval = Math.min(val, maxval);
        double percent = (minval / (double) maxval) * 100;
		System.out.printf("\r  %.1f%% %10d / %d", percent, minval, maxval);
		System.out.flush();
		if((int)percent % 25 == 0) {
			Toast toast = Toast.makeText(ct, (int) percent + "%", Toast.LENGTH_SHORT);
			toast.show();
		}
    }

	public static void tmp1(int left, Context ct) throws IOException {
		for (int index = 0; index < Math.floor((game_cxi_size / (float)READ_SIZE)) + 1; index++) {
			int to_read = Math.min(READ_SIZE, left);
			byte[] tmpread = new byte[to_read];
			rom.read(tmpread,0,to_read);
			game_cxi_hash.update(tmpread);
			cia.write(tmpread);
			left -= READ_SIZE;
			show_progress(game_cxi_size - left, game_cxi_size,ct);
			if (left <= 0) {
				System.out.println();
				break;
			}
		}
	}

	public static int tmp2() throws IOException {
        byte[] buffer = new byte[4];
        rom.readFully(buffer);
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return byteBuffer.getInt() * 0x200;
    }

	public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

	 public static byte[] decompress(byte[] compressedData) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

	private static byte[] concatenate(byte[] first, byte[] second) {
        byte[] result = new byte[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private static MessageDigest computeSha256() throws NoSuchAlgorithmException {
		return MessageDigest.getInstance("SHA-256");
    }
	@SuppressLint("SetTextI18n")
	private static void logging(String tx, TextView tv){
		tv.setText(tv.getText()+"\n"+tx);
	}

	@SuppressLint("SetTextI18n")
	public static void toCia(Uri input_file, File cia_file, TextView textViewResult, String fn, File f3ds_file, Context context) throws IOException, NoSuchAlgorithmException, DataFormatException, CloneNotSupportedException {
		rom = new RandomAccessFile(f3ds_file,"r");
		rom.seek(0x108);

		byte[] title_id = new byte[8];
		rom.read(title_id);
		for (int i = 0; i < title_id.length / 2; i++) {
			byte temp = title_id[i];
			title_id[i] = title_id[title_id.length - i - 1];
			title_id[title_id.length - i - 1] = temp;
		}

		rom.seek(0x120);
		int game_cxi_offset = tmp2();
		game_cxi_size = tmp2();
		int manual_cfa_offset = tmp2();
		int manual_cfa_size = tmp2();
		tmp2();
		int dlpchild_cfa_size = tmp2();

		logging("Converting " + fn + " (decrypted)...",textViewResult);

		rom.seek(game_cxi_offset + 0x200);
		extheader = new byte[0x400];
    	rom.readFully(extheader);

		ArrayList<Byte> extheader_list = new ArrayList<>();
		for (byte b : extheader) {
        	extheader_list.add(b);
    	}
		extheader_list.set(0xD,  (byte)(extheader_list.get(0xD) | 2));
		extheader = Bytes.toArray(extheader_list);

		byte[] new_extheader_hash = MessageDigest.getInstance("SHA-256").digest(extheader);

		byte[] save_size = new byte[4];
		System.arraycopy(extheader, 0x1C0, save_size, 0, 4);
		rom.seek(game_cxi_offset);

		ArrayList<Byte> ncchHeader = new ArrayList<>();
		byte[] extheader_buffer = new byte[0x200];
		int bytesRead = rom.read(extheader_buffer);
		for (int i = 0; i < bytesRead; i++) {
			ncchHeader.add(extheader_buffer[i]);
		}
		ncch_header = Bytes.toArray(ncchHeader);

		System.arraycopy(new_extheader_hash, 0, ncch_header, 0x160, 32);

		if (cia_file.exists()) {
            cia_file.delete();
			cia_file.createNewFile();
        }
		cia = new RandomAccessFile(cia_file,"rw");
		ByteBuffer chunk_records_buffer = ByteBuffer.allocate(12 + 4 + 32 + 12 + 4 + 32);
		chunk_records_buffer.put(new byte[0xC]);
		chunk_records_buffer.putInt(game_cxi_size);
		chunk_records_buffer.put(new byte[0x20]);
		chunk_records_buffer.put(hexStringToByteArray("000000010001000000000000"));
		chunk_records_buffer.putInt(manual_cfa_size);
		chunk_records_buffer.put(new byte[0x20]);
		byte[] chunk_records = chunk_records_buffer.array();

		byte[] decompressed = decompress(Base64.getDecoder().decode(ciainfo));
		ByteBuffer huanS = ByteBuffer.allocate(16 + 8 + 4 + 1 + 4 + 0x201F + decompressed.length + 2412 + chunk_records.length + 28);
		huanS.put(hexStringToByteArray("2020000000000000000A000050030000"));
		huanS.put(new byte[]{0x64,0x0b,0,0,0,0,0,0});
		huanS.putInt(Integer.reverseBytes(game_cxi_size + manual_cfa_size+ dlpchild_cfa_size));
		huanS.put(new byte[4]);
		huanS.put(new byte[]{(byte) 0xC0});
		huanS.put(new byte[0x201F]);
		huanS.put(decompressed);
		huanS.put(new byte[2412]);
		huanS.put(chunk_records);
		huanS.put(new byte[28]);
		cia.write(huanS.array());

		cia.seek(0x2F9F);
		cia.write((byte)0x02);
		cia.seek(0x2C1C);
		cia.write(title_id);
		cia.seek(0x2F4C);
		cia.write(title_id);
		cia.seek(0x2F5A);
		cia.write(save_size);

		cia.seek(cia.length());
		game_cxi_hash = computeSha256();
		game_cxi_hash.update(concatenate(ncch_header, extheader));
		cia.write(concatenate(ncch_header, extheader));
		logging("Writing Game Executable CXI...",textViewResult);

		rom.seek(game_cxi_offset + 0x200 + 0x400);
		int left = game_cxi_size - 0x200 - 0x400;
		tmp1(left,context);
		cia.seek(0x38D4);
		MessageDigest game_cxi_hash_cia_write = (MessageDigest) game_cxi_hash.clone();
		cia.write(game_cxi_hash_cia_write.digest());

		MessageDigest game_cxi_hash_arraycopy = (MessageDigest) game_cxi_hash.clone();
		System.arraycopy(game_cxi_hash_arraycopy.digest(), 0, chunk_records, 0x10, 32);

		cia.seek(cia.length());
		logging("Writing Manual CFA...",textViewResult);

		MessageDigest manual_cfa_hash = MessageDigest.getInstance("SHA-256");
		rom.seek(manual_cfa_offset);
		left = manual_cfa_size;
		tmp1(left,context);
		cia.seek(0x3904);
		MessageDigest manual_cfa_hash_cia_write = (MessageDigest) manual_cfa_hash.clone();
		cia.write(manual_cfa_hash_cia_write.digest());

		System.arraycopy(manual_cfa_hash.digest(), 0, chunk_records, 0x40, 32);
		byte[] chunk_records_hash = MessageDigest.getInstance("SHA-256").digest(chunk_records);
		cia.seek(0x2FC7);
		cia.write((byte) 0x02);
		cia.write(chunk_records_hash);
		cia.seek(0x2FA4);

		byte[] info_records_hash = MessageDigest.getInstance("SHA-256").digest(concatenate(concatenate(new byte[]{0x00, 0x00, 0x00, 0x02}, chunk_records_hash), new byte[0x8DC]));
		cia.write(info_records_hash);
		logging("Done converting files.",textViewResult);
		logging("\nPath: "+ cia_file.getPath(),textViewResult);
	}
}
