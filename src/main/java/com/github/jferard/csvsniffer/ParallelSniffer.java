/*******************************************************************************
 * CSV Sniffer - A simple sniffer to detect file encoding and CSV format of a file
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * 
 * This file is part of CSV Sniffer.
 * 
 * CSV Sniffer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CSV Sniffer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.csvsniffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class ParallelSniffer implements Sniffer {
	private Sniffer[] sniffers;

	public ParallelSniffer(Sniffer... sniffers) {
		this.sniffers = sniffers;
	}

	@Override
	public void sniff(InputStream inputStream, final int size) throws IOException {
		final int length = this.sniffers.length;
		PipedOutputStream[] pipedOutputStreams = new PipedOutputStream[length];
		Thread[] threads = new Thread[length];

		for (int i = 0; i < length; i++) {
			final Sniffer sniffer = this.sniffers[i];
			pipedOutputStreams[i] = new PipedOutputStream();
			final PipedInputStream pipedInputStream = new PipedInputStream(
					pipedOutputStreams[i]);

			threads[i] = new Thread() {
				@Override
				public void run() {
					try {
						sniffer.sniff(pipedInputStream, size);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};

			threads[i].start();
		}

		int c = inputStream.read();
		while (c != -1) {
			for (int i = 0; i < length; i++) {
				pipedOutputStreams[i].write(c);
			}
			c = inputStream.read();
		}
		for (int i = 0; i < length; i++) {
			pipedOutputStreams[i].close();
		}
		
		try {
			for (int i = 0; i < length; i++) {
				threads[i].join();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
